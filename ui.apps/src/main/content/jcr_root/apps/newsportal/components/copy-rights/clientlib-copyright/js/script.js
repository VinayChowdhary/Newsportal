document.addEventListener("DOMContentLoaded", function () {
    var copyRightsContainer = document.getElementById("copyRightsContainer");
    var pagePath = copyRightsContainer.getAttribute("data-page-path");

    console.log("Page Path:", pagePath);

    if (pagePath) {
        fetch(`/newsportal/copyrights?pagePath=${encodeURIComponent(pagePath)}`)
            .then(response => {
                console.log("Response status:", response.status); 
                return response.json();
            })
            .then(data => {
                console.log("Servlet Response:", data); 
                if (!data.error) {
                    document.getElementById("componentText").innerText = data.componentText;
                    document.getElementById("copyrightText").innerText = data.copyrightText;
                }
            })
            .catch(error => console.error("Error fetching copy-rights data:", error));
    } else {
        console.error("Page Path is missing!");
    }
});
