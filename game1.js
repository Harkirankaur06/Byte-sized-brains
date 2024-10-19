const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');

const gridSize = 20;
const tileCount = canvas.width / gridSize;

let snake = [{ x: 10, y: 10 }];
let food = { x: Math.floor(Math.random() * tileCount), y: Math.floor(Math.random() * tileCount) };
let direction = { x: 1, y: 0 }; // Starting direction is right
let running = true;

// Listen for keydown events for controlling snake
document.addEventListener('keydown', keyDownEvent);

function keyDownEvent(event) {
    switch (event.keyCode) {
        case 37: // Left arrow
            if (direction.x === 0) {
                direction = { x: -1, y: 0 };
            }
            break;
        case 38: // Up arrow
            if (direction.y === 0) {
                direction = { x: 0, y: -1 };
            }
            break;
        case 39: // Right arrow
            if (direction.x === 0) {
                direction = { x: 1, y: 0 };
            }
            break;
        case 40: // Down arrow
            if (direction.y === 0) {
                direction = { x: 0, y: 1 };
            }
            break;
    }
}

function drawGame() {
    if (!running) return;

    updateSnake();
    if (!checkCollision()) {
        drawCanvas();
        drawSnake();
        drawFood();
    } else {
        running = false;
        alert('Game Over!');
    }

    setTimeout(drawGame, 1000 / 10); // Update at 10 FPS
}

function updateSnake() {
    // Move the snake by adding a new head based on the direction
    const newHead = { x: snake[0].x + direction.x, y: snake[0].y + direction.y };

    // Wrap around the edges
    if (newHead.x >= tileCount) newHead.x = 0;
    if (newHead.x < 0) newHead.x = tileCount - 1;
    if (newHead.y >= tileCount) newHead.y = 0;
    if (newHead.y < 0) newHead.y = tileCount - 1;

    snake.unshift(newHead);

    // Check if the snake eats the food
    if (newHead.x === food.x && newHead.y === food.y) {
        food = { x: Math.floor(Math.random() * tileCount), y: Math.floor(Math.random() * tileCount) };
    } else {
        snake.pop(); // Remove the tail if no food is eaten
    }
}

function checkCollision() {
    // Check if snake collides with itself
    for (let i = 1; i < snake.length; i++) {
        if (snake[i].x === snake[0].x && snake[i].y === snake[0].y) {
            return true;
        }
    }
    return false;
}

function drawCanvas() {
    // Clear the canvas
    ctx.fillStyle = '#f1f1f1';
    ctx.fillRect(0, 0, canvas.width, canvas.height);
}

function drawSnake() {
    // Draw the snake
    ctx.fillStyle = 'green';
    for (let part of snake) {
        ctx.fillRect(part.x * gridSize, part.y * gridSize, gridSize, gridSize);
    }
}

function drawFood() {
    // Draw the food
    ctx.fillStyle = 'red';
    ctx.fillRect(food.x * gridSize, food.y * gridSize, gridSize, gridSize);
}

// Start the game
drawGame();
