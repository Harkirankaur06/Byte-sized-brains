// Initialize variables
let lastActivityTime = Date.now(); // Time the user was last active
let inactiveThreshold = 5 * 60 * 1000; // 5 minutes inactivity threshold (in milliseconds)
let sleepCycleStatus = document.getElementById('sleepCycleStatus');
let detectSleepCycleBtn = document.getElementById('detectSleepCycleBtn');

// Function to start detecting sleep cycles based on inactivity
function detectSleepCycle() {
    sleepCycleStatus.textContent = 'Monitoring your activity for sleep cycle detection...';

    // Start monitoring inactivity
    setInterval(() => {
        let currentTime = Date.now();
        let timeDiff = currentTime - lastActivityTime;

        // Check if the user was inactive for more than the threshold (5 minutes)
        if (timeDiff > inactiveThreshold) {
            let sleepDuration = timeDiff / 3600000; // Convert time difference to hours
            let startSleepTime = new Date(lastActivityTime).toLocaleTimeString();
            let endSleepTime = new Date(currentTime).toLocaleTimeString();

            // Check if sleep duration is less than 6 hours
            if (sleepDuration < 6) {
                sleepCycleStatus.textContent = `Sleep cycle detected from ${startSleepTime} to ${endSleepTime}. WARNING: Sleep cycle less than 6 hours! You may be facing serious health problems.`;
            } else {
                sleepCycleStatus.textContent = `Sleep cycle detected from ${startSleepTime} to ${endSleepTime}. Your sleep cycle is healthy.`;
            }

            // Log the detected sleep cycle for debugging
            console.log(`Sleep cycle detected: From ${startSleepTime} to ${endSleepTime} (Duration: ${sleepDuration.toFixed(2)} hours)`);
            
            // Reset the activity timer
            lastActivityTime = currentTime;
        }
    }, 1000); // Check every second
}

// Function to reset the activity timer whenever user interacts
function resetActivityTimer() {
    lastActivityTime = Date.now();
    console.log("User activity detected, resetting timer.");
}

// Add event listeners for user interactions
window.addEventListener('mousemove', resetActivityTimer);
window.addEventListener('keydown', resetActivityTimer);
window.addEventListener('click', resetActivityTimer);

// Start detecting sleep cycles when the button is clicked
detectSleepCycleBtn.addEventListener('click', detectSleepCycle);
