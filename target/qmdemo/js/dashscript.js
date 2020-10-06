/*!
 * Start Bootstrap - SB Admin v6.0.1 (https://startbootstrap.com/templates/sb-admin)
 * Copyright 2013-2020 Start Bootstrap
 * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
 */
 (function ($) {
    "use strict";
//    setTimeout(addNewNodesCards, 200);
//    setTimeout(eraseDeletedNodesCards, 200);
//    setTimeout(updateNodesCards, 200);
//         
    setInterval(addNewNodesCards, 200);
    setInterval(eraseDeletedNodesCards, 200);
    setInterval(updateNodesCards, 200);
    setInterval(updateJobSelectOptions, 5000);
    
    function convertoToMillis(timeUnit, duration)
    {
        function switchTimeUnit(timeUnit, duration)
        {
            switch (timeUnit) {
                case "Hours":
                    return Math.floor(duration * 60 * 60 * 1000);
                case "Minutes":
                    return Math.floor(duration * 60 * 1000);
                case "Seconds":
                    return Math.floor(duration * 1000);
                default:
                    return "OK";
            }
        }

        return switchTimeUnit(timeUnit, duration);
    }
    function setGlobalRefreshInfos(timeUnit, duration)
    {
        let timeLength = convertoToMillis(timeUnit, duration);
        alert(timeLength);
        let myVar = setInterval(updateJobExecutionCharts, timeLength);
        localStorage.setItem('myVar', myVar);
    }
    function discardGlobalRefreshInfos()
    {
        let foo = localStorage.getItem("myVar");
        clearInterval(foo);
    }
    $("#globalRefreshBoxButton").click(function () {
        let timeUnit = $("#globalRefreshTimeUnit").children("option:selected").val();
        let duration = $("#globalRefreshDuration").val();
        setGlobalRefreshInfos(timeUnit, duration);
    });
    $("#globalRefreshDiscardBoxButton").click(function () {

        discardGlobalRefreshInfos();
    });

    $("#globalSearchBoxButton").click(function () {
        searchGloballyForJobInfos();
    });

    function addNewNodesCards()
    {
        a.getNodesInfos(function (t) {
            let response = t.responseObject();
            $.each(response, function (i, node) {
                if (!$(".nodeCards div#nodeId" + node.nodeName).length)
                {
                    displayNodeCard(node);
                    $("#searchButtonFor" + node.nodeName).click(function () {
                        searchForJobInfos(node.nodeName);
                    });
                }
            });
        });
    }

    function populateGlobalSelectJobOptions()
    {
        let selectedJobOption = $("#globalBoxJobNames").children("option:selected").val();
        $("#globalBoxJobNames").empty();
        $("#globalBoxJobNames").append('<option value="" selected>----</option>');
        a.getAllJobNames(function (t) {
            let response = t.responseObject();
            $.each(response, function (i, item) {
                if (selectedJobOption === item)
                {
                    $("#globalBoxJobNames").append('<option value="' + item + '" selected>' + item + '</option>');
                } else
                {
                    $("#globalBoxJobNames").append('<option value="' + item + '">' + item + '</option>');
                }
            });
        });
    }    
    function populateSelectJobOptionsForNode(nodeName)
    {
        let selectedJobOption = $("#jobNameForNode" + nodeName).children("option:selected").val();
        $("#jobNameForNode" + nodeName).empty();
        $("#jobNameForNode" + nodeName).append('<option value="" selected>----</option>');
        a.getAllJobNames(function (t) {
            let response = t.responseObject();
            $.each(response, function (i, item) {
                if (selectedJobOption === item)
                {
                    $("#jobNameForNode" + nodeName).append('<option value="' + item + '" selected>' + item + '</option>');
                } else
                {
                    $("#jobNameForNode" + nodeName).append('<option value="' + item + '">' + item + '</option>');
                }
            });
        });
    }
    function updateNodesCards()
    {
        a.getNodesInfos(function (t) {
            let response = t.responseObject();
            $.each(response, function (i, node) {
                updateNodeCard(node);
            });

        });
    }
    /************************************global and node job selection selects**************************/
    function updateJobSelectOptions()
    {
        a.getNodesInfos(function (t) {
            let response = t.responseObject();
            $.each(response, function (i, node) {
                populateSelectJobOptionsForNode(node.nodeName);
            });

        });
        populateGlobalSelectJobOptions();
    }
    /**************************************************************************************************/
    function retriveNodesNames()
    {
        new Promise((resolve, reject) => {
            var nodeNames = [];
            a.getNodesInfos(function (t) {
                let node = t.responseObject();
                nodeNames.push(node.nodeName);
                resolve(nodeNames);
            });
        });
    }
    async function eraseDeletedNodesCards()
    {
        let nbNodesCards = $(".nodeCards").children('div').length;
        let nodeNames = await retriveNodesNames();
        let cardsIds = [];
        $.each($(".nodeCards").children('div'), function (index, value) {
            cardsIds.push(this.id);
        });
        let nodesCardsNames = [];
        $.each(cardsIds, function (index, value) {
            nodesCardsNames.push(value.substring(6, value.length - 7));
        });
        if (nbNodesCards > nodeNames)
        {
            $.each(nodesCardsNames, function (index, value) {
                if (jQuery.inArray(value, nodeNames) != -1) {
                    eraseNodeCard(value)
                }
            }
            );
        }

    }
    function displayNodeCard(node)
    {
        $(".nodeCards").append('<div class="row" id="nodeId' + node.nodeName + '"><div class="col-lg-12"><div class="card mb-4 shadow"> <div class="card-header"> <i class="fas fa-laptop mr-1"></i>' + node.nodeName + '</div> <div class="card-body"><div class="row"><div class="col-lg-2"><div class="card queue-node-card-height mr-1 shadow"> <div class="card-header"> <i class="fas fa-traffic-light mr-1"></i> Availability </div><div class="card-body"><div class="trafficlight"> <div class="red"></div> <div class="green"></div> <div class="yellow"></div></div><div class="offlineBy"></div></div></div></div> <div class="col-lg-2"><div class="card mr-1 queue-node-card-height shadow"> <div class="card-header"> <i class="fas fa-hourglass mr-1"></i> Queue State </div> <div class="card-body"><div class="queueTrafficlight"> <div class="red"></div> <div class="green"></div></div><div class="nbBuilds"></div></div></div></div><div class="col-lg-8"> <div class="row  pl-3"><form id="searchFormForNode' + node.nodeName + '"> <div class="form-row"> <div class="col-md-3"> <div class="form-group"> <label class="small mb-1" for="jobNameForNode' + node.nodeName + '">Select Job</label><select class="form-control py-3" id="jobNameForNode' + node.nodeName + '"> <option value="">---</option> </select></div> </div> <div class="col-md-3"> <div class="form-group"> <label class="small mb-1" for="startDateForNode' + node.nodeName + '">Start Date</label> <input class="form-control py-3" id="startDateForNode' + node.nodeName + '" type="date" /> </div> </div> <div class="col-md-4"> <div class="form-group"> <label class="small mb-1" for="endDateForNode' + node.nodeName + '">End Date</label> <input class="form-control py-3" id="endDateForNode' + node.nodeName + '" type="date" aria-describedby="emailHelp" /> </div> </div> <div class="col-md-2"> <div class="form-group mt-4 mb-0"> <a class="btn btn-secondary btn-block" id="searchButtonFor' + node.nodeName + '">Search</a> </div> </div> </div></form></div> <div class="row"> <div class="col-lg-3"> <div class="card mr-1 "> <div class="card-header"> <i class="fas fa-chart-pie mr-1"></i> TestCases status </div> <div id="tcrOnNodeName' + node.nodeName + '" class="card-body chart-card-height shadow"> </div></div> </div> <div class="col-lg-4"> <div class="card mr-1"> <div class="card-header"> <i class="fas fa-chart-pie mr-1"></i> Build status </div> <div id="bsOnNodeName' + node.nodeName + '" class="card-body chart-card-height shadow"> </div></div> </div> <div class="col-lg-5"> <div class="card mr-1"> <div class="card-header"> <i class="fas fa-chart-area mr-1"></i> Build results </div> <div id="brOnNodeName' + node.nodeName + '" class="card-body chart-card-height shadow"> <canvas id="myAreaChart" width="100%" height="30"></canvas></div></div></div></div></div></div></div></div></div></div>');
    }
    function updateNodeCard(node)
    {
        if (node.status === 'online')
        {
            $(".nodeCards div#nodeId" + node.nodeName + " div.trafficlight div.green").addClass("on");
            $(".nodeCards div#nodeId" + node.nodeName + " div.trafficlight div.red").removeClass("on");
            $(".nodeCards div#nodeId" + node.nodeName + " div.trafficlight div.yellow").removeClass("on");
            $(".nodeCards div#nodeId" + node.nodeName + " div.trafficlight").next(".offlineBy").empty();
        }
        if (node.status === 'offline')
        {
            if (node.offlineBy === "")
            {
                $(".nodeCards div#nodeId" + node.nodeName + " div.trafficlight div.green").removeClass("on");
                $(".nodeCards div#nodeId" + node.nodeName + " div.trafficlight div.red").addClass("on");
                $(".nodeCards div#nodeId" + node.nodeName + " div.trafficlight div.yellow").removeClass("on");
                $(".nodeCards div#nodeId" + node.nodeName + " div.trafficlight").next(".offlineBy").empty();
            } else
            {
                $(".nodeCards div#nodeId" + node.nodeName + " div.trafficlight div.green").removeClass("on");
                $(".nodeCards div#nodeId" + node.nodeName + " div.trafficlight div.red").removeClass("on");
                $(".nodeCards div#nodeId" + node.nodeName + "  div.trafficlight div.yellow").addClass("on");
                $(".nodeCards div#nodeId" + node.nodeName + " div.trafficlight").next(".offlineBy").empty();
                $(".nodeCards div#nodeId" + node.nodeName + " div.trafficlight").next(".offlineBy").text("disconnected by :"+ node.offlineBy);
            }
        }
        $(".nodeCards div#nodeId" + node.nodeName + " div.queueTrafficlight").next(".nbBuilds").empty();
        $(".nodeCards div#nodeId" + node.nodeName + " div.queueTrafficlight").next(".nbBuilds").text(node.nbBuilds + "#builds");
        if (node.nbBuilds === 0)
        {
            $(".nodeCards div#nodeId" + node.nodeName + " div.queueTrafficlight div.green").addClass("on");
            $(".nodeCards div#nodeId" + node.nodeName + " div.queueTrafficlight div.red").removeClass("on");
        } else
        {
            $(".nodeCards div#nodeId" + node.nodeName + " div.queueTrafficlight div.green").removeClass("on");
            $(".nodeCards div#nodeId" + node.nodeName + " div.queueTrafficlight div.red").addClass("on");
        }
    }
    function updateJobExecutionCharts()
    {
        a.getNodesInfos(function (t) {
            let jobName = $("select#globalBoxJobNames").val();
            let startDate = $("input#globalBoxStartDate").val();
            let endDate = $("input#globalBoxEndDate").val();
            let response = t.responseObject();
            if (jobName !== "" && startDate !== "" && endDate !== "")
            {
                $.each(response, function (i, node) {
                    displayJobsCharts(node.nodeName, jobName, startDate, endDate);
                });
            } else
            {
                $.each(response, function (i, node) {
                    let jobName = $("form#searchFormForNode" + node.nodeName + " select#jobNameForNode" + node.nodeName).val();
                    let startDate = $("form#searchFormForNode" + node.nodeName + " input#startDateForNode" + node.nodeName).val();
                    let endDate = $("form#searchFormForNode" + node.nodeName + " input#endDateForNode" + node.nodeName).val();
                    if (jobName !== "" && startDate !== "" && endDate !== "")
                    {
                        displayJobsCharts(node.nodeName, jobName, startDate, endDate);
                    }
                });
            }
        });
    }
    function searchForJobInfos(nodeName)
    {
        
        let jobName = $("form#searchFormForNode" + nodeName + " select#jobNameForNode" + nodeName).val();
        let startDate = $("form#searchFormForNode" + nodeName + " input#startDateForNode" + nodeName).val();
        let endDate = $("form#searchFormForNode" + nodeName + " input#endDateForNode" + nodeName).val();
        if (jobName === "")
        {
            alert("input " + nodeName + " job name")
        }
        if (startDate === "")
        {
            alert("input " + nodeName + " start date")
        }
        if (endDate === "")
        {
            alert("input " + nodeName + " start date")
        }
        if (jobName !== "" && startDate !== "" && endDate !== "")
        {
            displayJobsCharts(nodeName, jobName, startDate, endDate);
        }
    }
    function searchGloballyForJobInfos()
    {        
        a.getNodesInfos(function (t) {

            let jobName = $("select#globalBoxJobNames").val();
            let startDate = $("input#globalBoxStartDate").val();
            let endDate = $("input#globalBoxEndDate").val();
            let response = t.responseObject();

                if (jobName === "")
                {
                    alert("input global job name")
                }
                if (startDate === "")
                {
                    alert("input global start date")
                }
                if (endDate === "")
                {
                    alert("input global start date")
                }
                if (jobName !== "" && startDate !== "" && endDate !== "")
                {
                    $.each(response, function (i, node) {
                        displayJobsCharts(node.nodeName, jobName, startDate, endDate);
                    });
                }
            
        });
    
    }
    function displayJobsCharts(nodeName, jobName, startDate, endDate)
    {
        console.log("entered");
        a.getJobExecutionInfos(nodeName, jobName, startDate, endDate, function (t) {
            let response = t.responseObject();
            displayBuildResultsPieChart(nodeName, response.nbSuccessfulBuilds, response.nbFailedBuilds, response.nbAbortedBuilds, response.nbUnstableBuilds);
            let dates = [];
            let success = [];
            let failed = [];
            let aborted = [];
            let unstable = [];
            $.each(response.resultsDetails, function (i, ite) {     
                console.log(ite.date);
                let newDate = moment(ite.date).format('DD-MM-YY');                                
                dates.push(newDate);
                let hours = (ite.totalDuration / (1000 * 60 * 60));                                               
                switch (ite.result) {
                    case 'success':                        
                        success.push(hours);                        
                        break;
                    case 'failed': 
                        console.log(hours);
                        failed.push(hours);
                        break;
                    case 'aborted':
                        aborted.push(hours);
                        break;
                    case 'unstable':
                        unstable.push(hours);
                        break;
                }
            });                 
            displayBuildResultsDetailsChart(nodeName, dates, success, failed, aborted, unstable);
        });
    }  
    function displayBuildResultsPieChart(nodeName, a, b, c, d)
    {
        Highcharts.chart('bsOnNodeName' + nodeName, {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                type: 'pie'
            },
            legend: {
                enabled: true,
                align: 'center',
                verticalAlign: 'top',
                layout: 'horizontal',
                x: 0, y: 50
            },
            credits: {
                enabled: false
            },
            title: {
                text: ''
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.y:.1f}</b>'
            },
            accessibility: {
                point: {
                    valueSuffix: '%'
                }
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false                        
                    },
                    showInLegend: true,
                    colors: [
                        '#3EBD58',
                        '#B13838',                                                
                        '#ECECEC',
                        '#ACBE45'
                    ]
                }
            },
            series: [{
                    name: 'Builds',
                    colorByPoint: true,
                    data: [{
                            name: 'success',
                            y: a
                        }, {
                            name: 'failed',
                            y: b
                        }, {
                            name: 'aborted',
                            y: c
                        }, {
                            name: 'unstable',
                            y: d
                        }]
                }]
        });
    }
    function displayBuildResultsDetailsChart(nodeName, dates, a, b, c, d)
    {
        console.log(b);
        Highcharts.chart('brOnNodeName' + nodeName, {
            title: {
                text: 'details'
            },
            yAxis: {
                title: {
                    text: 'durtion'
                }
            },
            credits: {
                enabled: false
            },
            xAxis: {
                categories: dates
            },

            legend: {
                layout: 'horizontal',
                align: 'center',
                verticalAlign: 'bottom'
            },

            plotOptions: {
                series: {
                    label: {
                        connectorAllowed: false
                    }
                }
            },
            series: [{
                    name: 'success',
                    data: a,
                    color: '#3EBD58'

                }, {
                    name: 'failed',
                    data: b,
                    color: '#B13838'
                }, {
                    name: 'aborted',
                    data: c,
                    color: '#ECECEC'
                }, {
                    name: 'unstable',
                    data: d,
                    color: '#ACBE45'
                }]
            ,

            responsive: {
                rules: [{
                        condition: {
                            maxWidth: 500
                        },
                        chartOptions: {
                            legend: {
                                layout: 'horizontal',
                                align: 'center',
                                verticalAlign: 'bottom'
                            }
                        }
                    }]
            }

        });
    }
    function eraseNodeCard(nodeName)
    {
        $(".nodeCards").remove("div#nodeId" + nodeName);
    }
})(jQuery);