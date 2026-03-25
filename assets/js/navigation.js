(function () {
    var mobileBreakpoint = window.matchMedia("(max-width: 992px)");
    var navs = document.querySelectorAll(".top-nav");

    navs.forEach(function (nav, index) {
        var toggle = nav.querySelector(".top-nav-toggle");
        var links = nav.querySelector(".top-nav-links");

        if (!toggle || !links) {
            return;
        }

        if (!links.id) {
            links.id = "primary-nav-links-" + (index + 1);
        }
        toggle.setAttribute("aria-controls", links.id);

        function setExpanded(expanded) {
            nav.classList.toggle("is-open", expanded);
            toggle.setAttribute("aria-expanded", expanded ? "true" : "false");
        }

        toggle.addEventListener("click", function () {
            var isExpanded = toggle.getAttribute("aria-expanded") === "true";
            setExpanded(!isExpanded);
        });

        links.querySelectorAll("a").forEach(function (link) {
            link.addEventListener("click", function () {
                if (mobileBreakpoint.matches) {
                    setExpanded(false);
                }
            });
        });

        function syncWithViewport() {
            if (!mobileBreakpoint.matches) {
                setExpanded(false);
            }
        }

        if (typeof mobileBreakpoint.addEventListener === "function") {
            mobileBreakpoint.addEventListener("change", syncWithViewport);
        } else if (typeof mobileBreakpoint.addListener === "function") {
            mobileBreakpoint.addListener(syncWithViewport);
        }

        setExpanded(false);
    });
})();
