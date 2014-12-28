/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/28/14.
 */
window.vn_edu_uit_owleditor_view_diagram_SuggestionGraph = function () {
    var SVG = this.getElement();
    this.onStateChange = function () {
        var nodes = JSON.parse(this.getState().nodes);
        var edges = JSON.parse(this.getState().edges);

        suggestionGraph(nodes, edges);
    }
    var suggestionGraph = function (nodes, edges) {
        var g = new dagreD3.graphlib.Graph()
            .setGraph({})
            .setDefaultEdgeLabel(function () {
                return {};
            });

        // Set an object for the graph label
        g.setGraph({});
        // Default to assigning a new object as a label for each new edge.
        g.setDefaultEdgeLabel(function () {
            return {};
        });

        nodes.forEach(function (v) {
            g.setNode(v.id, {label: v.label, width: 100, height: 20});
        });
        edges.forEach(function (v) {
            // key = Object.keys(v)[0]

            g.setEdge(v.start, v.end, {label: v.label});
        });
        g.nodes().forEach(function (v) {
            console.log("Node " + v + ": " + JSON.stringify(g.node(v)));
        });
        g.edges().forEach(function (e) {
            console.log("Edge " + e.v + " -> " + e.w + ": " + JSON.stringify(g.edge(e)));
        });
        var viewerWidth = $(document).width();
        var viewerHeight = $(document).height();
        // Create the renderer
        var render = new dagreD3.render();

        // Set up an SVG group so that we can translate the final graph.
        var svg = d3.select(SVG).append("svg")
            .attr("width", viewerWidth)
            .attr("height", viewerHeight)
            .attr("class", "overlay");
        inner = svg.append("g");
        svg.selectAll
        // Set up zoom support
        var zoom = d3.behavior.zoom().on("zoom", function () {
            inner.attr("transform", "translate(" + d3.event.translate + ")" +
            "scale(" + d3.event.scale + ")");
        });
        svg.call(zoom);

        // Simple function to style the tooltip for the given node.
        var styleTooltip = function (name, description) {
            return "<p class='name'>" + name + "</p><p class='description'>" + description + "</p>";
        };

        // Run the renderer. This is what draws the final graph.
        render(inner, g);

        inner.selectAll("g.node")
            .attr("title", function (v) {
                return styleTooltip(v, g.node(v).description)
            })
            .each(function (v) {
                $(this).tipsy({gravity: "w", opacity: 1, html: true});
            });

        // Center the graph
        var initialScale = 0.75;
        zoom
            .translate([(svg.attr("width") - g.graph().width * initialScale) / 2, 20])
            .scale(initialScale)
            .event(svg);
        svg.attr('height', g.graph().height * initialScale + 40);

    }
}

