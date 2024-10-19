const startButton = document.getElementById('startButton');
const statusDiv = document.getElementById('status');

let audioContext;
let analyser;
let dataArray;

startButton.addEventListener('click', () => {
    startButton.disabled = true; // Disable the button
    statusDiv.textContent = 'Initializing...'; // Update status
    startMonitoringNoise();
});

function startMonitoringNoise() {
    // Create audio context
    audioContext = new (window.AudioContext || window.webkitAudioContext)();
    
    // Request access to the microphone
    navigator.mediaDevices.getUserMedia({ audio: true })
        .then(stream => {
            // Create media stream source
            const source = audioContext.createMediaStreamSource(stream);
            
            // Create analyser node
            analyser = audioContext.createAnalyser();
            analyser.fftSize = 2048;
            const bufferLength = analyser.frequencyBinCount;
            dataArray = new Uint8Array(bufferLength);
            
            // Connect the source to the analyser
            source.connect(analyser);
            monitorNoise();
        })
        .catch(err => {
            console.error('Error accessing microphone: ', err);
            statusDiv.textContent = 'Error accessing microphone. Please check your settings.';
        });
}

function monitorNoise() {
    analyser.getByteFrequencyData(dataArray);
    
    // Calculate average noise level
    let sum = 0;
    for (let i = 0; i < dataArray.length; i++) {
        sum += dataArray[i];
    }
    const average = sum / dataArray.length;

    // Update the status based on noise level
    if (average < 50) {
        statusDiv.textContent = 'Quiet environment detected.';
    } else if (average < 100) {
        statusDiv.textContent = 'Moderate noise level.';
    } else {
        statusDiv.textContent = 'Loud environment detected! Please reduce noise.';
    }

    // Call this function again on the next frame
    requestAnimationFrame(monitorNoise);
}
