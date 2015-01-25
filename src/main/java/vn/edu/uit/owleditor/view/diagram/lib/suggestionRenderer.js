/**
 * @author Chuong Dang, University of Information and Technology, HCMC Vietnam,
 *         Faculty of Computer Network and Telecommunication created on 12/28/14.
 */
window.vn_edu_uit_owleditor_view_diagram_SuggestionGraph = function () {
    var SVG_ELEMENT = this.getElement();

    this.onStateChange = function () {
        var data = JSON.parse(this.getState().data) || {data: {nodes: [], edges: []}, object: {nodes: [], edges: []}};

        console.log(data);
        var g = new dagreD3.graphlib.Graph()
            .setGraph({})
            .setDefaultEdgeLabel(function () {
                return {};
            });

        data.data.nodes.forEach(function (v) {
            g.setNode(v.id, {label: v.label});
        });
        
        data.data.edges.forEach(function (v) {
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
        var viewerWidth = $(".suggestion-graph-container").width();
        var viewerHeight = $(".suggestion-graph-container").height();

        // Create the renderer
        var render = new dagreD3.render();

        // Set up an SVG group so that we can translate the final graph.
        var svg = d3.select(SVG_ELEMENT).append("svg"),
            //.attr("width", viewerWidth)
            //.attr("height", viewerHeight)
            //.attr("class", "overlay"),
            inner = svg.select("g");

        // Run the renderer. This is what draws the final graph.
        render(inner, g);



        // Set up zoom support
        var zoom = d3.behavior.zoom().on("zoom", function() {
            inner.attr("transform", "translate(" + d3.event.translate + ")" +
            "scale(" + d3.event.scale + ")");
        });
        svg.call(zoom);
        
        // Center the graph
        var initialScale = 0.75;
        zoom.translate([(svg.attr("width") - g.graph().width * initialScale) / 2, 20])
            .scale(initialScale)
            .event(svg);
        svg.attr('height', g.graph().height * initialScale + 40);

    }
    
    //$(window).on("resize", function() {
    //    var targetWidth = svg.parent().width();
    //    svg.attr("width", targetWidth);
    //    svg.attr("height", targetWidth / aspect);
    //});

}

