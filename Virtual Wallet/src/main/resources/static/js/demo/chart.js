document.addEventListener("DOMContentLoaded", function () {
    try {
        var ctx = document.getElementById("earningsChart").getContext("2d");
        if (!ctx) {
            console.error("Canvas element not found! Ensure <canvas id='earningsChart'></canvas> exists.");
            return;
        }

        var earningsChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: ["January", "February", "March", "April", "May", "June"],
                datasets: [{
                    label: "Earnings ($)",
                    data: [500, 1000, 750, 1250, 1500, 2000],
                    borderColor: "#4e73df",
                    backgroundColor: "rgba(78, 115, 223, 0.05)",
                    pointBorderColor: "#4e73df",
                    pointBackgroundColor: "#4e73df",
                    pointRadius: 3,
                    fill: true
                }]
            },
            options: {
                maintainAspectRatio: false,
                scales: {
                    x: {
                        grid: { display: false }
                    },
                    y: {
                        ticks: {
                            callback: function(value) {
                                return '$' + value;
                            }
                        }
                    }
                }
            }
        });
    } catch (error) {
        console.error("Error initializing chart:", error);
    }
});
