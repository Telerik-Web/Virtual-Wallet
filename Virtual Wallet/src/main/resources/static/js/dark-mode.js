function toggleDarkMode() {
    console.log("Dark mode button clicked!"); // Debugging line
    document.body.classList.toggle("dark-mode");

    // Save theme preference to localStorage
    if (document.body.classList.contains("dark-mode")) {
        localStorage.setItem("theme", "dark");
    } else {
        localStorage.setItem("theme", "light");
    }
}

// Apply saved theme on page load
document.addEventListener("DOMContentLoaded", function () {
    console.log("Checking saved theme..."); // Debugging line
    if (localStorage.getItem("theme") === "dark") {
        document.body.classList.add("dark-mode");
    }
});
