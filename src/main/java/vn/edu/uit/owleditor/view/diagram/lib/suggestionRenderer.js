/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/28/14.
 */
window.vn_edu_uit_owleditor_view_diagram_SuggestionGraph = function () {
    var SVG_ELEMENT = this.getElement();
    var viewerWidth = $(".suggestion-graph-container").width();
    var viewerHeight = $(".suggestion-graph-container").height();

    
    this.onStateChange = function () {
        var data = JSON.parse(this.getState().data) || {data: {nodes: [], edges: []}, object: {nodes: [], edges: []}};

        console.log(data);
        d3.select("svg")
            .remove();

        var g = new dagreD3.graphlib.Graph()
            .setGraph({})
            .setDefaultEdgeLabel(function () {
                return {};
            });
        d3.select
        data.data.nodes.forEach(function (v) {
            g.setNode(v.id, {label: v.label});
        });
        data.data.edges.forEach(function (v) {
            console.log(v.start + " -> " + v.end);
            g.setEdge(v.start, v.end, {label: v.label});
        });
        data.object.nodes.forEach(function (v) {
            g.setNode(v.id, {label: v.label});
        });
        data.object.edges.forEach(function (v) {

            g.setEdge(v.start, v.end, {label: v.label});
        });
        g.nodes().forEach(function (v) {
            console.log("Node " + v + ": " + JSON.stringify(g.node(v)));
        });
        g.edges().forEach(function (e) {
            console.log("Edge " + e.v + " -> " + e.w + ": " + JSON.stringify(g.edge(e)));
        });

        var svg = d3.select(SVG_ELEMENT).append("svg")
                .attr("width", viewerWidth)
                .attr("height", viewerHeight),
            svgGroup = svg.append("g");

        // Create the renderer
        var render = new dagreD3.render();

        // Set up an SVG group so that we can translate the final graph.
        

        // Run the renderer. This is what draws the final graph.
        render(d3.select("svg g"), g);

        // Center the graph
        var xCenterOffset = (svg.attr("width") - g.graph().width) / 2;
        svgGroup.attr("transform", "translate(" + xCenterOffset + ", 20)");
        svg.attr("height", g.graph().height + 40);


        // Set up zoom support
        var zoom = d3.behavior.zoom().on("zoom", function () {
            svgGroup.attr("transform", "translate(" + d3.event.translate + ")" +
            "scale(" + d3.event.scale + ")");
        });
        svg.call(zoom);

    }


    //$(window).on("resize", function() {
    //    var targetWidth = svg.parent().width();
    //    svg.attr("width", targetWidth);
    //    svg.attr("height", targetWidth / aspect);
    //});

}

