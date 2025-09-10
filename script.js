const canvas = document.getElementById("starCanvas");
const ctx = canvas.getContext("2d");
canvas.width = window.innerWidth;
canvas.height = window.innerHeight;

const stars = [];
for (let i = 0; i < 150; i++) {
  stars.push({
    x: Math.random() * canvas.width,
    y: Math.random() * canvas.height,
    radius: Math.random() * 1.5,
    speed: Math.random() * 0.5 + 0.2,
  });
}

function drawStars() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  ctx.fillStyle = "#fff";
  for (let star of stars) {
    ctx.beginPath();
    ctx.arc(star.x, star.y, star.radius, 0, Math.PI * 2);
    ctx.fill();

    // Move star down
    star.y += star.speed;
    if (star.y > canvas.height) {
      star.y = 0;
      star.x = Math.random() * canvas.width;
    }
  }
  requestAnimationFrame(drawStars);
}
drawStars();

// Dragon movement
const dragon = document.getElementById("dragon");
let dragonX = window.innerWidth / 2;
let dragonY = window.innerHeight / 2;

function moveDragon() {
  // Random slight movement
  dragonX += Math.random() * 10 - 5;
  dragonY += Math.random() * 10 - 5;

  // Keep in bounds
  dragonX = Math.max(0, Math.min(window.innerWidth - 100, dragonX));
  dragonY = Math.max(0, Math.min(window.innerHeight - 100, dragonY));

  dragon.style.left = dragonX + "px";
  dragon.style.top = dragonY + "px";

  requestAnimationFrame(moveDragon);
}
moveDragon();

// Resize canvas on window resize
window.addEventListener("resize", () => {
  canvas.width = window.innerWidth;
  canvas.height = window.innerHeight;
});
