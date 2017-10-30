/*
 * This file is part of the Metasi package.
 *
 * Renatas Narmontas
 *
 * 2017 (c)
 */
(function ($) {

  /**
   * Hide elements on document load
   */
  $(document).ready( function () {
    $("#loader1").hide();
    $("#loader2").hide();
    $("#loader3").hide();
    $("#loader4").hide();
    $("#myModal").modal('hide');
    console.log("ready!");
  });

  /**
   * Show modal window
   */
  $("#process").click(function (e) {
    $("#myModal").modal();
  });

  /**
   * Fetch processed data and display it in DataTables
   */
  $("#results").click(function (e) {
    e.preventDefault();

    $("ag").show();
    $("hn").show();
    $("ou").show();
    $("vz").show();

    var loader1Div = $("#loader1");
    loader1Div.show();
    var loader2Div = $("#loader2");
    loader2Div.show();
    var loader3Div = $("#loader3");
    loader3Div.show();
    var loader4Div = $("#loader4");
    loader4Div.show();

    var result1Div = $("#result1");
    var result2Div = $("#result2");
    var result3Div = $("#result3");
    var result4Div = $("#result4");

    // Display results A-G
    $("#firstTable").DataTable({
      "destroy": "true",
      "initComplete": function(settings, json) {
        loader1Div.hide();
        result1Div.removeClass("hidden").addClass("visible");
      },
      "ajax": {
        "url": "rest/1",
        "type": "POST",
        "dataSrc": ""
      },
      "columns": [
        { "data": "word" },
        { "data": "count" }
      ],
      "lengthMenu": [
        [10, 50, -1],
        [10, 50, "All"]
      ],
      "order": [[1, "desc"]]
    }
    );

    // Display results H-N
    $("#secondTable").DataTable({
      "destroy": "true",
      "initComplete": function(settings, json) {
        loader2Div.hide();
        result2Div.removeClass("hidden").addClass("visible");
      },
      "ajax": {
        "url": "rest/2",
        "type": "POST",
        "dataSrc": ""
      },
      "columns": [
        { "data": "word" },
        { "data": "count" }
      ],
      "lengthMenu": [
        [10, 50, -1],
        [10, 50, "All"]
      ],
      "order": [[1, "desc"]]
    }
    );

    // Display results O-U
    $("#thirdTable").DataTable({
      "destroy": "true",
      "initComplete": function(settings, json) {
        loader3Div.hide();
        result3Div.removeClass("hidden").addClass("visible");
      },
      "ajax": {
        "url": "rest/3",
        "type": "POST",
        "dataSrc": ""
      },
      "columns": [
        { "data": "word" },
        { "data": "count" }
      ],
      "lengthMenu": [
        [10, 50, -1],
        [10, 50, "All"]
      ],
      "order": [[1, "desc"]]
    }
    );

    // Display results V-Z
    $("#fourthTable").DataTable({
      "destroy": "true",
      "initComplete": function(settings, json) {
        loader4Div.hide();
        result4Div.removeClass("hidden").addClass("visible");
      },
      "ajax": {
        "url": "rest/4",
        "type": "POST",
        "dataSrc": ""
      },
      "columns": [
        { "data": "word" },
        { "data": "count" }
      ],
      "lengthMenu": [
        [10, 50, -1],
        [10, 50, "All"]
      ],
      "order": [[1, "desc"]]
    }
    );

  });

})(window.jQuery);
