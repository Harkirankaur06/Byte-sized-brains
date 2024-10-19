// Create a 4x4 grid for the memory match game
const gridContainer = document.querySelector('.grid-container');

// Function to create the grid
function createGrid(rows, cols) {
    for (let i = 0; i < rows * cols; i++) {
        const gridItem = document.createElement('div');
        gridItem.classList.add('grid-item');
        gridItem.textContent = '?'; // Placeholder for the hidden content
        gridItem.addEventListener('click', flipCard); // Add event listener for click
        gridContainer.appendChild(gridItem);
    }
}

// Example flip card logic (you can customize this for your memory match logic)
function flipCard(event) {
    const card = event.target;
    card.textContent = Math.floor(Math.random() * 10); // For now, randomly show numbers
}

// Create the 4x4 grid
createGrid(4, 4);
