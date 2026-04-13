
document.querySelectorAll("button").forEach((button) => {
  button.addEventListener("click", handleClick);
});

function handleClick() {
  alert("I got clicked!");
}