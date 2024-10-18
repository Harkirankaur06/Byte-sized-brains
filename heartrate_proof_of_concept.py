import cv2
import numpy as np
from scipy.signal import find_peaks, butter, filtfilt
import time

# Capture video from the camera
cap = cv2.VideoCapture(0)

# Low-pass filter to remove noise (frequencies outside normal heart rate range)
def butter_bandpass(lowcut, highcut, fs, order=5):
    nyquist = 0.5 * fs
    low = lowcut / nyquist
    high = highcut / nyquist
    b, a = butter(order, [low, high], btype='band')
    return b, a

def apply_bandpass_filter(data, lowcut=0.8, highcut=3.0, fs=30, order=3):
    b, a = butter_bandpass(lowcut, highcut, fs, order=order)
    y = filtfilt(b, a, data)
    return y

# Get the heart rate based on the peaks in the signal
def get_heart_rate(filtered_signal, fps):
    # Find peaks in the filtered signal
    peaks, _ = find_peaks(filtered_signal, distance=fps/2)
    print(f"Detected Peaks: {len(peaks)}")  # Debugging line to check peak detection
    if len(peaks) > 1:
        # Calculate the time between peaks
        peak_times = np.diff(peaks) / fps
        # Convert to heart rate (BPM)
        heart_rate = 60 / np.mean(peak_times)
        return heart_rate
    else:
        return None

# Parameters
fps = 30  # frames per second
roi_size = (100, 100)  # Region of Interest size
buffer_time = 30  # seconds for data collection
buffer_size = fps * buffer_time  # frames for buffer
min_frames_required = int(buffer_size * 0.9)  # Allow 90% of the frames to be enough

# Buffer to hold the green channel intensity values
green_intensity_values = []
heart_rate_values = []

# Countdown timer for user to place finger
countdown_time = 10  # seconds for the countdown

def countdown_and_capture():
    print("Place your finger on the camera.")
    for i in range(countdown_time, 0, -1):
        print(f"Recording starts in {i} seconds...")
        time.sleep(1)

    print("Recording heart rate...")

    start_time = time.time()
    frame_count = 0
    while time.time() - start_time < buffer_time:
        ret, frame = cap.read()
        if not ret:
            break

        frame_count += 1

        # Get the Region of Interest (ROI)
        roi = frame[100:100 + roi_size[0], 100:100 + roi_size[1]]  # Sample area on the face
        cv2.rectangle(frame, (100, 100), (100 + roi_size[0], 100 + roi_size[1]), (0, 255, 0), 2)

        # Convert the ROI to RGB and get the green channel
        green_channel = roi[:, :, 1]
        avg_green_intensity = np.mean(green_channel)
        green_intensity_values.append(avg_green_intensity)

        # Display the video frame with the ROI box
        cv2.imshow('Heart Rate Detection', frame)

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    print(f"Captured Frames: {frame_count}")  # Debugging line to check frame count
    print("Recording finished. Please remove your finger.")

# Start countdown and capture
countdown_and_capture()

# Process the captured data
if len(green_intensity_values) >= min_frames_required:
    print(f"Processing {len(green_intensity_values)} frames")  # Debugging line to check buffer size
    green_signal = np.array(green_intensity_values[:min_frames_required])

    # Apply a bandpass filter to isolate heart rate frequencies
    filtered_signal = apply_bandpass_filter(green_signal, lowcut=0.8, highcut=3.0, fs=fps, order=3)
    print(f"Filtered Signal Length: {len(filtered_signal)}")  # Debugging line to check signal length

    # Get the heart rate from the filtered signal
    heart_rate = get_heart_rate(filtered_signal, fps)

    if heart_rate:
        heart_rate_values.append(heart_rate)

        # Calculate the average of the collected heart rate readings
        avg_heart_rate = np.mean(heart_rate_values)
        print(f"Final Average Heart Rate: {avg_heart_rate:.2f} BPM")
    else:
        print("Heart rate could not be detected. Please try again.")
else:
    print(f"Not enough frames captured. Only {len(green_intensity_values)} frames collected.")

# Release resources
cap.release()
cv2.destroyAllWindows()
