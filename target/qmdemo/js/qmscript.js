/*!
 * Start Bootstrap - SB Admin v6.0.1 (https://startbootstrap.com/templates/sb-admin)
 * Copyright 2013-2020 Start Bootstrap
 * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-sb-admin/blob/master/LICENSE)
 */
function reschedule(id, operation)
    {
        let row = $('#' + id).closest('tr');
        let next = row.next();
        let prev = row.prev();
        let currentQueueId = row.find("td:nth-child(5)").text();
        console.log(currentQueueId);
        let queueId1 = next.find("td:nth-child(5)").text();
        console.log(queueId1);
        let queueId2 = prev.find("td:nth-child(5)").text();
        console.log(queueId2);
        if (operation === "up" && queueId2 !== "")
        {
            a.reScheduleQueue(currentQueueId, queueId2, operation);
        }
        if (operation === "down" && queueId1 !== "")
        {
            a.reScheduleQueue(currentQueueId, queueId1, operation);

        }

    }
(function ($) {
    "use strict";
    $("table").on("click", ".up,.down", function () {
        var row = $(this).parents("tr:first");
        if ($(this).is('.up')) {
            row.insertBefore(row.prev());
        } else {
            row.insertAfter(row.next());
        }
    });        
})(jQuery);
