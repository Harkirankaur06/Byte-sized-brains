let video = document.getElementById('video');
let canvas = document.getElementById('canvas');
let ctx = canvas.getContext('2d');

let fps = 30;
let buffer_time = 30;  // Buffer duration (in seconds)
let countdown_time = 10;
let roi_size = 100;
let green_intensity_values = [];
let frameBuffer = [];

const countdownAndCapture = () => {
    document.getElementById('message').innerText = `Recording starts in ${countdown_time} seconds...`;

    let countdown = setInterval(() => {
        countdown_time--;
        if (countdown_time > 0) {
            document.getElementById('message').innerText = `Recording starts in ${countdown_time} seconds...`;
        } else {
            clearInterval(countdown);
            document.getElementById('message').innerText = "Recording heart rate....Wait for around 30 seconds.";
            startCapturing();
        }
    }, 1000);
};

const startCapturing = () => {
    let startTime = Date.now();
    let frameCount = 0;

    let captureInterval = setInterval(() => {
        if (Date.now() - startTime >= buffer_time * 1000) {
            clearInterval(captureInterval);
            processSignal();
        } else {
            captureFrame();
            frameCount++;
        }
    }, 1000 / fps);
};

const captureFrame = () => {
    ctx.drawImage(video, 0, 0, canvas.width, canvas.height);

    // Capture the Region of Interest (ROI)
    let imageData = ctx.getImageData(100, 100, roi_size, roi_size);
    let pixels = imageData.data;
    let greenSum = 0;

    for (let i = 0; i < pixels.length; i += 4) {
        greenSum += pixels[i + 1];  // Green channel
    }

    let avgGreen = greenSum / (pixels.length / 4);
    green_intensity_values.push(avgGreen);
};

const processSignal = () => {
    if (green_intensity_values.length > 0) {
        // Apply a simple smoothing filter (moving average)
        let smoothedSignal = smoothSignal(green_intensity_values, 5);

        // Detect peaks to calculate heart rate
        let heartRate = calculateHeartRate(smoothedSignal);
        document.getElementById('heartRate').innerText = `Detected Heart Rate: ${heartRate ? heartRate.toFixed(2) : 'Not Detected'} BPM`;
    } else {
        document.getElementById('message').innerText = "Not enough data captured. Please try again.";
    }
};

const smoothSignal = (data, windowSize) => {
    let smoothed = [];
    for (let i = 0; i < data.length; i++) {
        let start = Math.max(0, i - windowSize);
        let end = Math.min(data.length, i + windowSize);
        let avg = data.slice(start, end).reduce((sum, val) => sum + val, 0) / (end - start);
        smoothed.push(avg);
    }
    return smoothed;
};

const calculateHeartRate = (signal) => {
    let peaks = [];
    let threshold = 1.02 * Math.min(...signal);  // Threshold slightly above the minimum

    for (let i = 1; i < signal.length - 1; i++) {
        if (signal[i] > signal[i - 1] && signal[i] > signal[i + 1] && signal[i] > threshold) {
            peaks.push(i);
        }
    }

    // Calculate BPM from peak distance
    if (peaks.length > 1) {
        let peakIntervals = peaks.map((peak, i) => (i > 0 ? (peak - peaks[i - 1]) / fps : null)).filter(x => x);
        let avgPeakInterval = peakIntervals.reduce((a, b) => a + b, 0) / peakIntervals.length;
        return 60 / avgPeakInterval;  // Convert to beats per minute
    } else {
        return 0;
    }
};

// Initialize video stream
navigator.mediaDevices.getUserMedia({ video: true }).then(stream => {
    video.srcObject = stream;
    video.play();

    // Start countdown
    countdownAndCapture();
}).catch(err => {
    console.error("Error accessing webcam: ", err);
    document.getElementById('message').innerText = "Webcam access denied.";
});
