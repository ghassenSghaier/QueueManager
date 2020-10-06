/*!
 * Start Bootstrap - SB Admin v6.0.1 (https://startbootstrap.com/templates/sb-admin)
 * Copyright 2013-2020 Start Bootstrap
 * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
 */
(function ($) {
    "use strict";
    function populateNodeSelectNamesOptions()
    {
        let selectedNodeOption = $("#globalBoxNodeNames").children("option:selected").val();
        $("#globalBoxNodeNames").empty();
        $("#globalBoxNodeNames").append('<option value="" selected>----</option>');
        a.getAllNodeNames(function (t) {
            let response = t.responseObject();
            $.each(response, function (i, item) {
                if (selectedNodeOption === item)
                {
                    $("#globalBoxNodeNames").append('<option value="' + item + '" selected>' + item + '</option>');
                } else
                {
                    $("#globalBoxNodeNames").append('<option value="' + item + '">' + item + '</option>');
                }
            });
        });
    }
    function setBuildsInProgress(nodeName)
    {
        $("#buildInProgressTable tbody").empty();
        a.getBuildsInProgress(nodeName, function (t) {            
            let response = t.responseObject();            
            $.each(response, function (i, item) {   
                console.log(item);
                $("#buildInProgressTable tbody").append("<tr><td>" + item.jobName + "</td><td>" + item.number + "</td><td><a href=\"" + item.buildUrl + "\">link</a></td><td>" + item.startedAt + "</td><td>" + item.progress + "</td><td>" + item.estimatedEndAt + "</td></tr>");
            });
        });
    }
    function setNodeInfos(nodeName)
    {
        $("#nodeInfosTable tbody").empty();
        a.getNodeInfo(nodeName, function (t) {
            let response = t.responseObject();
            $("#nodeInfosTable tbody").append("<tr><td>" + response.nodeName + "</td><td>" + response.status + "</td><td>" + response.totalExecutors + "</td><td>" + response.busyExecutors + "</td><td>" + response.idelExecutors + "</td></tr>");
        });
    }
    $("#searchBoxButton").click(function () {
        let nodeName = $("#globalBoxNodeNames").val();
        setNodeInfos(nodeName);
        if (nodeName === "")
        {
            alert("node name is requierd");
        } else
        {
            setBuildsInProgress(nodeName);
        }
    });
})(jQuery);
