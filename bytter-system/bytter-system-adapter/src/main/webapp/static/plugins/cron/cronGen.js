layui.define(["jquery", "element", "form"], function (exports) {
    var jQuery = layui.jquery;
    var $ = layui.jquery;
    form = layui.form();
    element = layui.element();
    (function ($) {
        var resultsName = "";
        var inputElement;
        var displayElement;
        $.fn.extend({
            cronGen: function (options) {
                if (options == null) {
                    options = {};
                }
                options = $.extend({}, $.fn.cronGen.defaultOptions, options);
                //create top menu
                var cronContainer = $("<div/>", {id: "CronContainer", style: "width:100%;height:100%;"});
                var mainDiv = $("<div/>", {id: "CronGenMainDiv", style: "width:100%;height:100%;"});
                var tabdiv = $("<div/>", {"class": "layui-tab", id: "CronGenTabsTitle"});
                var topMenu = $("<ul/>", {"class": "layui-tab-title", id: "CronGenTabs"});
                $('<li/>', {'class': 'layui-this'}).html($('<a id="SecondlyTab" href="javascript:;">' + bt.lang("common.unit.second") + '</a>')).appendTo(topMenu);
                $('<li/>').html($('<a id="MinutesTab" href="javascript:;">' + bt.lang("common.unit.minute") + '</a>')).appendTo(topMenu);
                $('<li/>').html($('<a id="HourlyTab" href="javascript:;">' + bt.lang("common.unit.hour") + '</a>')).appendTo(topMenu);
                $('<li/>').html($('<a id="DailyTab" href="javascript:;">' + bt.lang("common.unit.day") + '</a>')).appendTo(topMenu);
                $('<li/>').html($('<a id="MonthlyTab" href="javascript:;">' + bt.lang("common.unit.month") + '</a>')).appendTo(topMenu);
                $('<li/>').html($('<a id="WeeklyTab" href="javascript:;">' + bt.lang("common.unit.week") + '</a>')).appendTo(topMenu);
                //$('<li/>').html($('<a id="YearlyTab" href="#Yearly">' + bt.lang("common.unit.year") +  '</a>')).appendTo(topMenu);
                $(tabdiv).appendTo(mainDiv);
                $(topMenu).appendTo(tabdiv);
                var tabContent = $("<div/>", {"class": "layui-tab-content", style: "border:0px; margin-top:-10px;"});
                //creating the secondsTab
                var secondsTab = $("<div/>", {"class": "layui-tab-item layui-show", id: "Secondly"});
                var seconds1 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "1",
                    name: "second",
                    title: bt.lang("common.adjective.per.second") + " " + bt.lang("common.cron.second.wildcards.allowed")
                }).attr("lay-filter", "filter_input").appendTo(seconds1);
                $(seconds1).appendTo(secondsTab);

                /*var seconds2 = $("<div/>",{"class":"line"});
                $("<input/>",{type : "radio", value : "2", name : "second", title: "周期 从"}).attr("lay-filter","filter_input").appendTo(seconds2);
                $("<input/>",{type : "text", "class":"layui-input layui-input-inline", id : "secondStart_0", value : "1", style:"width:35px;"}).attr("lay-filter","filter_input").appendTo(seconds2);
                $(seconds2).append("-");
                $("<input/>",{type : "text", "class":"layui-input layui-input-inline", id : "secondEnd_0", value : "2", style:"width:35px;"}).attr("lay-filter","filter_input").appendTo(seconds2);
                $(seconds2).append("秒");
                $(seconds2).appendTo(secondsTab);*/

                var seconds3 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "3",
                    name: "second",
                    title: bt.lang("common.adjective.from")
                }).attr("lay-filter", "filter_input").appendTo(seconds3);
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "secondStart_1",
                    value: "0",
                    style: "width:35px;"
                }).attr("lay-filter", "filter_input").appendTo(seconds3);
                $(seconds3).append(bt.lang("common.unit.second")).append(bt.lang("common.verb.start")).append(",").append(bt.lang("common.adjective.per"));
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "secondEnd_1",
                    value: "1",
                    style: "width:35px;"
                }).attr("lay-filter", "filter_input").appendTo(seconds3);
                $(seconds3).append(bt.lang("common.unit.second")).append(bt.lang("common.verb.execute.once"));
                $(seconds3).appendTo(secondsTab);

                var seconds4 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "4",
                    name: "second",
                    id: "sencond_appoint",
                    title: bt.lang("common.cron.text.assign")
                }).attr("lay-filter", "sencond_appoint").appendTo(seconds4);
                $(seconds4).appendTo(secondsTab);

                $(secondsTab).append('<div class="imp secondList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="0" title="00"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="1" title="01"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="2" title="02"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="3" title="03"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="4" title="04"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="5" title="05"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="6" title="06"></div>');
                $(secondsTab).append('<div class="imp secondList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="7" title="07"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="8" title="08"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="9" title="09"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="10" title="10"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="11" title="11"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="12" title="12"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="13" title="13"></div>');
                $(secondsTab).append('<div class="imp secondList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="14" title="14"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="15" title="15"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="16" title="16"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="17" title="17"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="18" title="18"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="19" title="19"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="20" title="20"></div>');
                $(secondsTab).append('<div class="imp secondList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="21" title="21"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="22" title="22"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="23" title="23"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="24" title="24"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="25" title="25"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="26" title="26"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="27" title="27"></div>');
                $(secondsTab).append('<div class="imp secondList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="28" title="28"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="29" title="29"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="30" title="30"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="31" title="31"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="32" title="32"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="33" title="33"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="34" title="34"></div>');
                $(secondsTab).append('<div class="imp secondList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="35" title="35"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="36" title="36"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="37" title="37"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="38" title="38"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="39" title="39"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="40" title="40"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="41" title="41"></div>');
                $(secondsTab).append('<div class="imp secondList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="42" title="42"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="43" title="43"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="44" title="44"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="45" title="45"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="46" title="46"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="47" title="47"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="48" title="48"></div>');
                $(secondsTab).append('<div class="imp secondList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="49" title="49"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="50" title="50"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="51" title="51"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="52" title="52"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="53" title="53"><input type="checkbox"  lay-skin="primary"disabled="disabled" style="margin-left: 5px"  value="54" title="54"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="55" title="55"></div>');
                $(secondsTab).append('<div class="imp secondList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="56" title="56"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="57" title="57"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="58" title="58"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="59" title="59"></div>');

                $.each($(secondsTab).find("input[type='checkbox']"), function () {
                    $(this).attr("lay-filter", "sencond_checkbox");
                });
                $("<input/>", {type: "hidden", id: "secondHidden", name: "second"}).appendTo(secondsTab);
                $(secondsTab).appendTo(tabContent);

                //creating the minutesTab
                var minutesTab = $("<div/>", {"class": "layui-tab-item", id: "Minutes"});

                var minutes1 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "1",
                    name: "min",
                    title: bt.lang("common.adjective.per.minute") + " " + bt.lang("common.cron.minute.wildcards.allowed")
                }).attr("lay-filter", "filter_input").appendTo(minutes1);
                $(minutes1).appendTo(minutesTab);

                var minutes2 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "2",
                    name: "min",
                    title: bt.lang("common.unit.period") + " " + bt.lang("common.adjective.from")
                }).attr("lay-filter", "filter_input").appendTo(minutes2);
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "minStart_0",
                    value: "1",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(minutes2);
                $(minutes2).append("-");
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "minEnd_0",
                    value: "2",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(minutes2);
                $(minutes2).append(bt.lang("common.unit.minute"));
                $(minutes2).appendTo(minutesTab);

                var minutes3 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "3",
                    name: "min",
                    title: bt.lang("common.adjective.from")
                }).attr("lay-filter", "filter_input").appendTo(minutes3);
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "minStart_1",
                    value: "0",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(minutes3);
                $(minutes3).append(bt.lang("common.unit.minute")).append(bt.lang("common.verb.start")).append(",").append(bt.lang("common.adjective.per"));
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "minEnd_1",
                    value: "1",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(minutes3);
                $(minutes3).append(bt.lang("common.unit.minute")).append(bt.lang("common.verb.execute.once"));
                $(minutes3).appendTo(minutesTab);

                var minutes4 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "4",
                    name: "min",
                    id: "min_appoint",
                    title: bt.lang("common.cron.text.assign")
                }).attr("lay-filter", "min_appoint").appendTo(minutes4);
                $(minutes4).appendTo(minutesTab);

                $(minutesTab).append('<div class="imp minList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="0" title="00"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="1" title="01"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="2" title="02"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="3" title="03"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="4" title="04"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="5" title="05"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="6" title="06"></div>');
                $(minutesTab).append('<div class="imp minList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="7" title="07"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="8" title="08"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="9" title="09"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="10" title="10"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="11" title="11"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="12" title="12"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="13" title="13"></div>');
                $(minutesTab).append('<div class="imp minList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="14" title="14"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="15" title="15"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="16" title="16"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="17" title="17"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="18" title="18"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="19" title="19"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="20" title="20"></div>');
                $(minutesTab).append('<div class="imp minList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="21" title="21"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="22" title="22"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="23" title="23"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="24" title="24"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="25" title="25"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="26" title="26"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="27" title="27"></div>');
                $(minutesTab).append('<div class="imp minList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="28" title="28"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="29" title="29"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="30" title="30"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="31" title="31"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="32" title="32"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="33" title="33"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="34" title="34"></div>');
                $(minutesTab).append('<div class="imp minList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="35" title="35"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="36" title="36"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="37" title="37"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="38" title="38"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="39" title="39"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="40" title="40"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="41" title="41"></div>');
                $(minutesTab).append('<div class="imp minList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="42" title="42"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="43" title="43"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="44" title="44"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="45" title="45"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="46" title="46"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="47" title="47"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="48" title="48"></div>');
                $(minutesTab).append('<div class="imp minList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="49" title="49"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="50" title="50"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="51" title="51"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="52" title="52"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="53" title="53"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="54" title="54"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="55" title="55"></div>');
                $(minutesTab).append('<div class="imp minList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="56" title="56"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="57" title="57"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="58" title="58"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="59" title="59"></div>');

                $.each($(minutesTab).find("input[type='checkbox']"), function () {
                    $(this).attr("lay-filter", "min_checkbox");
                });
                $("<input/>", {type: "hidden", id: "minHidden", name: "minute"}).appendTo(minutesTab);
                $(minutesTab).appendTo(tabContent);

                //creating the hourlyTab
                var hourlyTab = $("<div/>", {"class": "layui-tab-item", id: "Hourly"});

                var hourly1 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "1",
                    name: "hour",
                    title: bt.lang("common.adjective.per.hour") + " " + bt.lang("common.cron.hour.wildcards.allowed")
                }).attr("lay-filter", "filter_input").appendTo(hourly1);
                $(hourly1).appendTo(hourlyTab);

                var hourly2 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "2",
                    name: "hour",
                    title: bt.lang("common.unit.period") + " " + bt.lang("common.adjective.from")
                }).attr("lay-filter", "filter_input").appendTo(hourly2);
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "hourStart_0",
                    value: "1",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(hourly2);
                $(hourly2).append("-");
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "hourEnd_0",
                    value: "2",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(hourly2);
                $(hourly2).append(bt.lang("common.unit.hour"));
                $(hourly2).appendTo(hourlyTab);

                var hourly3 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "3",
                    name: "hour",
                    title: bt.lang("common.adjective.from")
                }).attr("lay-filter", "filter_input").appendTo(hourly3);
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "hourStart_1",
                    value: "0",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(hourly3);
                $(hourly3).append(bt.lang("common.unit.hour")).append(bt.lang("common.verb.start")).append(",").append(bt.lang("common.adjective.per"));
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "hourEnd_1",
                    value: "1",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(hourly3);
                $(hourly3).append(bt.lang("common.unit.hour")).append(bt.lang("common.verb.execute.once"));
                $(hourly3).appendTo(hourlyTab);

                var hourly4 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "4",
                    name: "hour",
                    id: "hour_appoint",
                    title: bt.lang("common.cron.text.assign")
                }).attr("lay-filter", "hour_appoint").appendTo(hourly4);
                $(hourly4).appendTo(hourlyTab);

                $(hourlyTab).append('<div class="imp hourList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="0" title="00"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="1" title="01"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="2" title="02"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="3" title="03"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="4" title="04"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="5" title="05"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="6" title="06"></div>');
                $(hourlyTab).append('<div class="imp hourList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="7" title="07"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="8" title="08"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="9" title="09"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="10" title="10"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="11" title="11"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="12" title="12"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="13" title="13"></div>');
                $(hourlyTab).append('<div class="imp hourList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="14" title="14"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="15" title="15"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="16" title="16"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="17" title="17"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="18" title="18"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="19" title="19"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="20" title="20"></div>');
                $(hourlyTab).append('<div class="imp hourList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="21" title="21"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="22" title="22"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="23" title="23"></div>');
                $.each($(hourlyTab).find("input[type='checkbox']"), function () {
                    $(this).attr("lay-filter", "hour_checkbox");
                });
                $("<input/>", {type: "hidden", id: "hourHidden", name: "hour"}).appendTo(hourlyTab);
                $(hourlyTab).appendTo(tabContent);

                //creating the dailyTab
                var dailyTab = $("<div/>", {"class": "layui-tab-item", id: "Daily"});

                var daily1 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "1",
                    name: "day",
                    title: bt.lang("common.adjective.per.day") + " " + bt.lang("common.cron.day.wildcards.allowed")
                }).attr("lay-filter", "filter_input").appendTo(daily1);
                $(daily1).appendTo(dailyTab);

                var daily5 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "2",
                    name: "day",
                    title: bt.lang("common.cron.text.not.assign")
                }).attr("lay-filter", "filter_input").appendTo(daily5);
                $(daily5).appendTo(dailyTab);

                var daily2 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "3",
                    name: "day",
                    title: bt.lang("common.unit.period") + " " + bt.lang("common.adjective.from")
                }).attr("lay-filter", "filter_input").appendTo(daily2);
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "dayStart_0",
                    value: "1",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(daily2);
                $(daily2).append("-");
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "dayEnd_0",
                    value: "2",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(daily2);
                $(daily2).append(bt.lang("common.unit.day"));
                $(daily2).appendTo(dailyTab);

                var daily3 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "4",
                    name: "day",
                    title: bt.lang("common.adjective.from")
                }).attr("lay-filter", "filter_input").appendTo(daily3);
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "dayStart_1",
                    value: "1",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(daily3);
                $(daily3).append(bt.lang("common.unit.day")).append(bt.lang("common.verb.start")).append(",").append(bt.lang("common.adjective.per"));
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "dayEnd_1",
                    value: "1",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(daily3);
                $(daily3).append(bt.lang("common.unit.day")).append(bt.lang("common.verb.execute.once"));
                $(daily3).appendTo(dailyTab);

                var daily6 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "5",
                    name: "day",
                    title: bt.lang("common.adjective.per.month")
                }).attr("lay-filter", "filter_input").appendTo(daily6);
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "dayStart_2",
                    value: "1",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(daily6);
                $(daily6).append(bt.lang("common.unit.number")).append(bt.lang("common.cron.last.working.day"));
                $(daily6).appendTo(dailyTab);

                var daily7 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "6",
                    name: "day",
                    title: bt.lang("common.cron.last.day.of.the.month")
                }).attr("lay-filter", "filter_input").appendTo(daily7);
                $(daily7).appendTo(dailyTab);

                var daily4 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "7",
                    name: "day",
                    id: "day_appoint",
                    title: bt.lang("common.cron.text.assign")
                }).attr("lay-filter", "day_appoint").appendTo(daily4);
                $(daily4).appendTo(dailyTab);

                $(dailyTab).append('<div class="imp dayList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="1" title="01"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="2" title="02"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="3" title="03"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="4" title="04"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="5" title="05"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="6" title="06"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="7" title="07"></div>');
                $(dailyTab).append('<div class="imp dayList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="8" title="08"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="9" title="09"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="10" title="10"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="11" title="11"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="12" title="12"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="13" title="13"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="14" title="14"></div>');
                $(dailyTab).append('<div class="imp dayList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="15" title="15"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="16" title="16"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="17" title="17"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="18" title="18"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="19" title="19"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="20" title="20"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="21" title="21"></div>');
                $(dailyTab).append('<div class="imp dayList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="22" title="22"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="23" title="23"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="24" title="24"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="25" title="25"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="26" title="26"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="27" title="27"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="28" title="28"></div>');
                $(dailyTab).append('<div class="imp dayList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="29" title="29"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="30" title="30"></div>');

                $(dailyTab).append('<div class="imp dayList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="31" title="31"></div>');
                $.each($(dailyTab).find("input[type='checkbox']"), function () {
                    $(this).attr("lay-filter", "day_checkbox");
                });
                $("<input/>", {type: "hidden", id: "dayHidden", name: "days"}).appendTo(dailyTab);
                $(dailyTab).appendTo(tabContent);

                //creating the monthlyTab
                var monthlyTab = $("<div/>", {"class": "layui-tab-item", id: "Monthly"});

                var monthly1 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "1",
                    name: "month",
                    title: bt.lang("common.adjective.per.month") + " " + bt.lang("common.cron.month.wildcards.allowed")
                }).attr("lay-filter", "filter_input").appendTo(monthly1);
                $(monthly1).appendTo(monthlyTab);

                var monthly2 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "2",
                    name: "month",
                    title: bt.lang("common.cron.text.not.assign")
                }).attr("lay-filter", "filter_input").appendTo(monthly2);
                $(monthly2).appendTo(monthlyTab);

                var monthly3 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "3",
                    name: "month",
                    title: bt.lang("common.unit.period") + " " + bt.lang("common.adjective.from")
                }).attr("lay-filter", "filter_input").appendTo(monthly3);
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "monthStart_0",
                    value: "1",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(monthly3);
                $(monthly3).append("-");
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "monthEnd_0",
                    value: "2",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(monthly3);
                $(monthly3).append(bt.lang("common.unit.month"));
                $(monthly3).appendTo(monthlyTab);

                var monthly4 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "4",
                    name: "month",
                    title: bt.lang("common.adjective.from")
                }).attr("lay-filter", "filter_input").appendTo(monthly4);
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "monthStart_1",
                    value: "1",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(monthly4);
                $(monthly4).append(bt.lang("common.unit.day")).append(bt.lang("common.verb.start")).append(",").append(bt.lang("common.adjective.per"));
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "monthEnd_1",
                    value: "1",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(monthly4);
                $(monthly4).append(bt.lang("common.unit.month")).append(bt.lang("common.verb.execute.once"));
                $(monthly4).appendTo(monthlyTab);

                var monthly5 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "5",
                    name: "month",
                    id: "month_appoint",
                    title: bt.lang("common.cron.text.assign")
                }).attr("lay-filter", "month_appoint").appendTo(monthly5);
                $(monthly5).appendTo(monthlyTab);

                $(monthlyTab).append('<div class="imp monthList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="1" title="01"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="2" title="02"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="3" title="03"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="4" title="04"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="5" title="05"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="6" title="06"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="7" title="07"></div>');
                $(monthlyTab).append('<div class="imp monthList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="8" title="08"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="9" title="09"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="10" title="10"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="11" title="11"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="12" title="12"></div>');
                $.each($(monthlyTab).find("input[type='checkbox']"), function () {
                    $(this).attr("lay-filter", "month_checkbox");
                });
                $("<input/>", {type: "hidden", id: "monthHidden", name: "month"}).appendTo(monthlyTab);
                $(monthlyTab).appendTo(tabContent);

                //creating the weeklyTab
                var weeklyTab = $("<div/>", {"class": "layui-tab-item", id: "Weekly"});

                var weekly1 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "1",
                    name: "week",
                    title: bt.lang("common.adjective.per.week") + " " + bt.lang("common.cron.week.wildcards.allowed")
                }).attr("lay-filter", "filter_input").appendTo(weekly1);
                $(weekly1).appendTo(weeklyTab);

                var weekly2 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "2",
                    name: "week",
                    title: bt.lang("common.cron.text.not.assign")
                }).attr("lay-filter", "filter_input").appendTo(weekly2);
                $(weekly2).appendTo(weeklyTab);

                var weekly3 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "3",
                    name: "week",
                    title: bt.lang("common.unit.period") + " " + bt.lang("common.adjective.from") + bt.lang("common.unit.sunday")
                }).attr("lay-filter", "filter_input").appendTo(weekly3);
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "weekStart_0",
                    value: "1",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(weekly3);
                $(weekly3).append("-");
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "weekEnd_0",
                    value: "2",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(weekly3);
                $(weekly3).appendTo(weeklyTab);

                var weekly4 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "4",
                    name: "week",
                    title: bt.lang("common.table.the")
                }).attr("lay-filter", "filter_input").appendTo(weekly4);
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "weekStart_1",
                    value: "1",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(weekly4);
                $(weekly4).append(bt.lang("common.unit.week")).append(bt.lang("common.auxiliary.of")).append(bt.lang("common.unit.sunday"));
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "weekEnd_1",
                    value: "1",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(weekly4);
                $(weekly4).appendTo(weeklyTab);

                var weekly5 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "5",
                    name: "week",
                    title: bt.lang("common.cron.last.week.of.the.month")
                }).attr("lay-filter", "filter_input").appendTo(weekly5);
                $("<input/>", {
                    type: "text",
                    "class": "layui-input layui-input-inline",
                    id: "weekStart_2",
                    value: "1",
                    style: "width:35px; height:20px; text-align: center; margin: 0 3px;"
                }).attr("lay-filter", "filter_input").appendTo(weekly5);
                $(weekly5).appendTo(weeklyTab);

                var weekly6 = $("<div/>", {"class": "line"});
                $("<input/>", {
                    type: "radio",
                    value: "6",
                    name: "week",
                    id: "week_appoint",
                    title: bt.lang("common.cron.text.assign")
                }).attr("lay-filter", "week_appoint").appendTo(weekly6);
                $(weekly6).appendTo(weeklyTab);

                $(weeklyTab).append('<div class="imp weekList"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="1" title="周日"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="2" title="周一"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="3" title="周二"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="4" title="周三"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="5" title="周四"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="6" title="周五"><input type="checkbox" lay-skin="primary" disabled="disabled" style="margin-left: 5px"  value="7" title="周六"></div>');
                $.each($(weeklyTab).find("input[type='checkbox']"), function () {
                    $(this).attr("lay-filter", "week_checkbox");
                });
                $("<input/>", {type: "hidden", id: "weekHidden", name: "week"}).appendTo(weeklyTab);
                $(weeklyTab).appendTo(tabContent);
                //creating the yearlyTab
                /*var yearlyTab = $("<div/>", { class: "layui-tab-item", id: "Yearly" });

                var yearly1 = $("<div/>",{class:"line"});
                $("<input/>",{type : "radio", value : "1", name : "year", title: "不指定 允许的通配符[, - * /] 非必填"}).attr("lay-filter","filter_input").appendTo(yearly1);
                $(yearly1).appendTo(yearlyTab);

                var yearly3 = $("<div/>",{"class":"line"});
                $("<input/>",{type : "radio", value : "2", name : "year", title: "每年"}).attr("lay-filter","filter_input").appendTo(yearly3);
                $(yearly3).appendTo(yearlyTab);

                var yearly2 = $("<div/>",{"class":"line"});
                $("<input/>",{type : "radio", value : "3", name : "year", title: "周期从"}).attr("lay-filter","filter_input").appendTo(yearly2);
                $("<input/>",{type : "text", class:"layui-input layui-input-inline", id : "yearStart_0", value : "2016", style:"width:45px; height:20px;"}).attr("lay-filter","filter_input").appendTo(yearly2);
                $(yearly2).append("-");
                $("<input/>",{type : "text", class:"layui-input layui-input-inline", id : "yearEnd_0", value : "2017", style:"width:45px; height:20px;"}).attr("lay-filter","filter_input").appendTo(yearly2);
                $(yearly2).append("年");
                $(yearly2).appendTo(yearlyTab);
                $("<input/>",{type : "hidden", id : "yearHidden"}).appendTo(yearlyTab);
                $(yearlyTab).appendTo(tabContent);*/
                $("<input/>", {type: "hidden", id: "yearHidden"}).appendTo(tabContent);
                //creating the button and results input
                resultsName = $(this).prop("id");
                $(this).prop("name", resultsName);

                $(tabContent).appendTo(mainDiv);
                $(tabdiv).append(tabContent);
                $(cronContainer).append(tabdiv);

                var that = $(this);
                // Hide the original input
                that.hide();

                // Replace the input with an input group
                var $g = $("<div/>").addClass("input-group");
                var $spantip = $("<span style='color:#c2c2c2'>" + bt.lang("common.cron.last.5.runs") + "</span><span class=\"input-group-btn chinesetip\"></span>");
                var $tip = $("<div/>").addClass("input-group");
                $spantip.appendTo($tip);
                // Add an input
                var $i = $("<input>", {
                    type: 'text',
                    disabled: "disabled",
                    style: "width:79%",
                    "class": "layui-input layui-input-inline cron_input",
                    placeholder: bt.lang("common.cron.text.assign")
                }).attr("lay-verify", "required").val($(that).val());
                $i.appendTo($g);
                // Add the button
                var $b = $("<button class=\"layui-btn\">" + bt.lang("common.button.select") + "</button>");
                // Put button inside span
                var $s = $("<span>").addClass("input-group-btn");
                var popover = $("<div>").addClass("popover");
                cronContainer.appendTo(popover);
                $b.appendTo($s);
                $b.after(popover);
                $s.appendTo($g);

                $(this).before($g);
                $(this).before($tip);

                inputElement = that;
                displayElement = $i;
                form.render();
                $b.on('click', function (e) {
                    if ($(".popover").is(":visible")) {
                        $(".popover").stop().slideUp("slow");
                    } else {
                        $(".popover").stop().slideDown("slow");
                    }
                    e.preventDefault();
                    //fillDataOfMinutesAndHoursSelectOptions();
                    //fillDayWeekInMonth();
                    //fillInWeekDays();
                    //fillInMonths();
                    $.fn.cronGen.tools.cronParse(inputElement.val());
                    //绑定指定事件
                    if (!$.fn.cronGen.initEventCount) {
                        $.fn.cronGen.tools.initChangeEvent();
                        $("#CronGenMainDiv select,input").change(function (e) {
                            generate();
                        });
                        $.fn.cronGen.initEventCount = true;
                    }
                    return false;
                });
                $.fn.cronGen.tools.translateToChinese(inputElement.val(), function (strE) {
                    if (strE) {
                        $(".chinesetip").html(strE);
                    }
                });
                $.fn.cronGen.tools.cronParse(inputElement.val());
                return false;
            }
        });


        var fillInMonths = function () {
            var days = [
                {text: "一月", val: "1"},
                {text: "二月", val: "2"},
                {text: "三月", val: "3"},
                {text: "四月", val: "4"},
                {text: "五月", val: "5"},
                {text: "六月", val: "6"},
                {text: "七月", val: "7"},
                {text: "八月", val: "8"},
                {text: "九月", val: "9"},
                {text: "十月", val: "10"},
                {text: "十一月", val: "11"},
                {text: "十二月", val: "12"}
            ];
            $(".months").each(function () {
                fillOptions(this, days);
            });
        };

        var fillOptions = function (elements, options) {
            for (var i = 0; i < options.length; i++)
                $(elements).append("<option value='" + options[i].val + "'>" + options[i].text + "</option>");
        };
        var fillDataOfMinutesAndHoursSelectOptions = function () {
            for (var i = 0; i < 60; i++) {
                if (i < 24) {
                    $(".hours").each(function () {
                        $(this).append(timeSelectOption(i));
                    });
                }
                $(".minutes").each(function () {
                    $(this).append(timeSelectOption(i));
                });
            }
        };
        var fillInWeekDays = function () {
            var days = [
                {text: "周一", val: "2"},
                {text: "周二", val: "3"},
                {text: "周三", val: "4"},
                {text: "周四", val: "5"},
                {text: "周五", val: "6"},
                {text: "周六", val: "7"},
                {text: "周天", val: "1"}
            ];
            $(".week-days").each(function () {
                fillOptions(this, days);
            });

        };
        var fillDayWeekInMonth = function () {
            var days = [
                {text: "第一个", val: "1"},
                {text: "第二个", val: "2"},
                {text: "第三个", val: "3"},
                {text: "第四个", val: "4"}
            ];
            $(".day-order-in-month").each(function () {
                fillOptions(this, days);
            });
        };
        var displayTimeUnit = function (unit) {
            if (unit.toString().length == 1)
                return "0" + unit;
            return unit;
        };
        var timeSelectOption = function (i) {
            return "<option id='" + i + "'>" + displayTimeUnit(i) + "</option>";
        };

        var generate = function () {
            var activeTab = $("ul#CronGenTabs li.layui-this a").prop("id");
            var results = "";
            switch (activeTab) {
                case "SecondlyTab":
                    switch ($("input:radio[name=second]:checked").val()) {
                        case "1":
                            $.fn.cronGen.tools.everyTime("second");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "2":
                            $.fn.cronGen.tools.cycle("second");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "3":
                            $.fn.cronGen.tools.startOn("second");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "4":
                            $.fn.cronGen.tools.initCheckBox("second");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                    }
                    break;
                case "MinutesTab":
                    switch ($("input:radio[name=min]:checked").val()) {
                        case "1":
                            $.fn.cronGen.tools.everyTime("min");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "2":
                            $.fn.cronGen.tools.cycle("min");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "3":
                            $.fn.cronGen.tools.startOn("min");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "4":
                            $.fn.cronGen.tools.initCheckBox("min");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                    }
                    break;
                case "HourlyTab":
                    switch ($("input:radio[name=hour]:checked").val()) {
                        case "1":
                            $.fn.cronGen.tools.everyTime("hour");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "2":
                            $.fn.cronGen.tools.cycle("hour");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "3":
                            $.fn.cronGen.tools.startOn("hour");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "4":
                            $.fn.cronGen.tools.initCheckBox("hour");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                    }
                    break;
                case "DailyTab":
                    switch ($("input:radio[name=day]:checked").val()) {
                        case "1":
                            $.fn.cronGen.tools.everyTime("day");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "2":
                            $.fn.cronGen.tools.unAppoint("day");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "3":
                            $.fn.cronGen.tools.cycle("day");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "4":
                            $.fn.cronGen.tools.startOn("day");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "5":
                            $.fn.cronGen.tools.workDay("day");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "6":
                            $.fn.cronGen.tools.lastDay("day");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "7":
                            $.fn.cronGen.tools.initCheckBox("day");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                    }
                    break;
                case "WeeklyTab":
                    switch ($("input:radio[name=week]:checked").val()) {
                        case "1":
                            $.fn.cronGen.tools.everyTime("week");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "2":
                            $.fn.cronGen.tools.unAppoint("week");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "3":
                            $.fn.cronGen.tools.cycle("week");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "4":
                            $.fn.cronGen.tools.startOn("week");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "5":
                            $.fn.cronGen.tools.lastWeek("week");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "6":
                            $.fn.cronGen.tools.initCheckBox("week");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                    }
                    break;
                case "MonthlyTab":
                    switch ($("input:radio[name=month]:checked").val()) {
                        case "1":
                            $.fn.cronGen.tools.everyTime("month");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "2":
                            $.fn.cronGen.tools.unAppoint("month");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "3":
                            $.fn.cronGen.tools.cycle("month");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "4":
                            $.fn.cronGen.tools.startOn("month");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "5":
                            $.fn.cronGen.tools.initCheckBox("month");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                    }
                    break;
                case "YearlyTab":
                    switch ($("input:radio[name=year]:checked").val()) {
                        case "1":
                            $.fn.cronGen.tools.unAppoint("year");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "2":
                            $.fn.cronGen.tools.everyTime("year");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                        case "3":
                            $.fn.cronGen.tools.cycle("year");
                            results = $.fn.cronGen.tools.cronResult();
                            break;
                    }
                    break;
            }
            // Update original control
            $.fn.cronGen.tools.translateToChinese(results, function (strE) {
                if (strE) {
                    $(".chinesetip").html(strE);
                }
            });

            inputElement.val(results);
            // Update display
            displayElement.val(results);
            form.render('radio');
            form.render('checkbox');
        };
    })(jQuery);
    (function ($) {
        $.fn.cronGen.defaultOptions = {
            direction: 'bottom'
        };
        $.fn.cronGen.initEventCount = false;
        $.fn.cronGen.tools = {
            /**
             * 表达式转中文描述
             */
            translateToChinese: function (cronExp, callback) {
                if (!cronExp) {
                    return "";
                }
                //获取数据
                $.post(basePath + "/scheduler/systask/cron/view", {cron: cronExp, count: 5}, function (result) {
                    var tipHtml = "";
                    if (result && result.length > 0) {
                        $.each(result, function (i, item) {
                            tipHtml += item + "<br/>";
                        });
                        tipHtml = "<br/>" + tipHtml.replace(/<br\/>$/, "");
                    } else {
                        tipHtml = bt.lang("common.cron.expression.error");
                    }
                    callback && callback(tipHtml);
                }, 'JSON');
            },
            /**
             * 每周期
             */
            everyTime: function (dom) {
                $("#" + dom + "Hidden").val("*");
                $.fn.cronGen.tools.clearCheckbox(dom);
            },
            /**
             * 不指定
             */
            unAppoint: function (dom) {
                var val = "?";
                if (dom == "year") {
                    val = "";
                }
                $("#" + dom + "Hidden").val(val);
                $.fn.cronGen.tools.clearCheckbox(dom);
            },
            /**
             * 周期
             */
            cycle: function (dom) {
                var start = $("#" + dom + "Start_0").val();
                var end = $("#" + dom + "End_0").val();
                $("#" + dom + "Hidden").val(start + "-" + end);
                $.fn.cronGen.tools.clearCheckbox(dom);
            },
            /**
             * 从开始
             */
            startOn: function (dom) {
                var start = $("#" + dom + "Start_1").val();
                var end = $("#" + dom + "End_1").val();
                $("#" + dom + "Hidden").val(start + "/" + end);
                $.fn.cronGen.tools.clearCheckbox(dom);
            },
            /**
             * 最后一天
             */
            lastDay: function (dom) {
                $("#" + dom + "Hidden").val("L");
                $.fn.cronGen.tools.clearCheckbox(dom);
            },
            /**
             * 每周的某一天
             */
            weekOfDay: function (dom) {
                var start = $("#" + dom + "Start_0").val();
                var end = $("#" + dom + "End_0").val();
                $("#" + dom + "Hidden").val(start + "#" + end);
                $.fn.cronGen.tools.clearCheckbox(dom);
            },
            /**
             * 最后一周
             */
            lastWeek: function (dom) {
                var start = $("#" + dom + "Start_2").val();
                $("#" + dom + "Hidden").val(start + "L");
                $.fn.cronGen.tools.clearCheckbox(dom);
            },
            /**
             * 工作日
             */
            workDay: function (dom) {
                var start = $("#" + dom + "Start_2").val();
                $("#" + dom + "Hidden").val(start + "W");
                $.fn.cronGen.tools.clearCheckbox(dom);
            },
            initChangeEvent: function () {
                var secondList = $(".secondList input");//$(".secondList").children();
                //指定秒事件
                form.on('radio(sencond_appoint)', function (data) {
                    if (this.checked) {
                        if ($(secondList).filter(":checked").length == 0) {
                            $(secondList.eq(0)).prop("checked", true);
                        }
                        form.render('checkbox');
                        $("#secondHidden").val($(secondList.eq(0)).val());
                        secondList.eq(0).change();
                    }
                });
                //指定区域的checkbox事件
                form.on("checkbox(sencond_checkbox)", function () {
                    var sencond_appoint = $("#sencond_appoint").prop("checked");
                    if (sencond_appoint) {
                        var vals = [];
                        secondList.each(function () {
                            if (this.checked) {
                                vals.push(this.value);
                            }
                        });
                        var val = "?";
                        if (vals.length > 0 && vals.length < 59) {
                            val = vals.join(",");
                        } else if (vals.length == 59) {
                            val = "*";
                        }
                        $("#secondHidden").val(val);
                        $(this).change();
                    }
                    form.render('checkbox');
                });

                var minList = $(".minList input");//$(".minList").children();
                form.on('radio(min_appoint)', function (data) {
                    if (this.checked) {
                        if ($(minList).filter(":checked").length == 0) {
                            $(minList.eq(0)).attr("checked", true);
                        }
                        form.render('checkbox');
                        $("#minHidden").val($(minList.eq(0)).val());
                        minList.eq(0).change();
                    }
                });
                form.on("checkbox(min_checkbox)", function () {
                    var min_appoint = $("#min_appoint").prop("checked");
                    if (min_appoint) {
                        var vals = [];
                        minList.each(function () {
                            if (this.checked) {
                                vals.push(this.value);
                            }
                        });
                        var val = "?";
                        if (vals.length > 0 && vals.length < 59) {
                            val = vals.join(",");
                        } else if (vals.length == 59) {
                            val = "*";
                        }
                        $("#minHidden").val(val);
                        $(this).change();
                    }
                    form.render('checkbox');
                });
                var hourList = $(".hourList input");//$(".hourList").children();
                form.on('radio(hour_appoint)', function (data) {
                    if (this.checked) {
                        if ($(hourList).filter(":checked").length == 0) {
                            $(hourList.eq(0)).attr("checked", true);
                        }
                        form.render('checkbox');
                        $("#hourHidden").val($(hourList.eq(0)).val());
                        hourList.eq(0).change();
                    }
                });
                form.on("checkbox(hour_checkbox)", function () {
                    var hour_appoint = $("#hour_appoint").prop("checked");
                    if (hour_appoint) {
                        var vals = [];
                        hourList.each(function () {
                            if (this.checked) {
                                vals.push(this.value);
                            }
                        });
                        var val = "?";
                        if (vals.length > 0 && vals.length < 24) {
                            val = vals.join(",");
                        } else if (vals.length == 24) {
                            val = "*";
                        }
                        $("#hourHidden").val(val);
                        $(this).change();
                    }
                    form.render('checkbox');
                });
                var dayList = $(".dayList input");//$(".dayList").children();
                form.on('radio(day_appoint)', function (data) {
                    if (this.checked) {
                        if ($(dayList).filter(":checked").length == 0) {
                            $(dayList.eq(0)).attr("checked", true);
                        }
                        form.render('checkbox');
                        $("#dayHidden").val($(dayList.eq(0)).val());
                        dayList.eq(0).change();
                    }
                });
                form.on("checkbox(day_checkbox)", function () {
                    var day_appoint = $("#day_appoint").prop("checked");
                    if (day_appoint) {
                        var vals = [];
                        dayList.each(function () {
                            if (this.checked) {
                                vals.push(this.value);
                            }
                        });
                        var val = "?";
                        if (vals.length > 0 && vals.length < 31) {
                            val = vals.join(",");
                        } else if (vals.length == 31) {
                            val = "*";
                        }
                        $("#dayHidden").val(val);
                        $(this).change();
                    }
                    form.render('checkbox');
                });

                var monthList = $(".monthList input");//$(".monthList").children();
                form.on('radio(month_appoint)', function (data) {
                    if (this.checked) {
                        if ($(monthList).filter(":checked").length == 0) {
                            $(monthList.eq(0)).attr("checked", true);
                        }
                        form.render('checkbox');
                        $("#monthHidden").val($(monthList.eq(0)).val());
                        monthList.eq(0).change();
                    }
                });
                form.on("checkbox(month_checkbox)", function () {
                    var month_appoint = $("#month_appoint").prop("checked");
                    if (month_appoint) {
                        var vals = [];
                        monthList.each(function () {
                            if (this.checked) {
                                vals.push(this.value);
                            }
                        });
                        var val = "?";
                        if (vals.length > 0 && vals.length < 12) {
                            val = vals.join(",");
                        } else if (vals.length == 12) {
                            val = "*";
                        }
                        $("#monthHidden").val(val);
                        $(this).change();
                    }
                    form.render('checkbox');
                });
                var weekList = $(".weekList input");//$(".weekList").children();
                form.on('radio(week_appoint)', function (data) {
                    if (this.checked) {
                        if ($(weekList).filter(":checked").length == 0) {
                            $(weekList.eq(0)).attr("checked", true);
                        }
                        form.render('checkbox');
                        $("#weekHidden").val($(weekList.eq(0)).val());
                        weekList.eq(0).change();
                    }
                });
                form.on("checkbox(week_checkbox)", function () {
                    var week_appoint = $("#week_appoint").prop("checked");
                    if (week_appoint) {
                        var vals = [];
                        weekList.each(function () {
                            if (this.checked) {
                                vals.push(this.value);
                            }
                        });
                        var val = "?";
                        if (vals.length > 0 && vals.length < 7) {
                            val = vals.join(",");
                        } else if (vals.length == 7) {
                            val = "*";
                        }
                        $("#weekHidden").val(val);
                        $(this).change();
                    }
                    form.render('checkbox');
                });
                //每个input事件 除去指定的
                form.on('radio(filter_input)', function (data) {
                    $(this).change();
                });
            },
            initObj: function (strVal, strid) {
                var ary = null;
                var objRadio = $("input[name='" + strid + "'");
                if (strVal == "*") {
                    objRadio.eq(0).attr("checked", "checked");
                } else if (strVal.split('-').length > 1) {
                    ary = strVal.split('-');
                    objRadio.eq(1).attr("checked", "checked");
                    $("#" + strid + "Start_0").val(ary[0]);
                    $("#" + strid + "End_0").val(ary[1]);
                } else if (strVal.split('/').length > 1) {
                    ary = strVal.split('/');
                    objRadio.eq(2).attr("checked", "checked");
                    $("#" + strid + "Start_1").val(ary[0]);
                    $("#" + strid + "End_1").val(ary[1]);
                } else {
                    objRadio.eq(3).attr("checked", "checked");
                    if (strVal != "?") {
                        ary = strVal.split(",");
                        for (var i = 0; i < ary.length; i++) {
                            $("." + strid + "List input[value='" + ary[i] + "']").attr("checked", "checked");
                        }
                        $.fn.cronGen.tools.initCheckBox(strid);
                    }
                }
            },
            initDay: function (strVal) {
                var ary = null;
                var objRadio = $("input[name='day']");
                if (strVal == "*") {
                    objRadio.eq(0).attr("checked", "checked");
                } else if (strVal == "?") {
                    objRadio.eq(1).attr("checked", "checked");
                } else if (strVal.split('-').length > 1) {
                    ary = strVal.split('-');
                    objRadio.eq(2).attr("checked", "checked");
                    $("#dayStart_0").val(ary[0]);
                    $("#dayEnd_0").val(ary[1]);
                } else if (strVal.split('/').length > 1) {
                    ary = strVal.split('/');
                    objRadio.eq(3).attr("checked", "checked");
                    $("#dayStart_1").val(ary[0]);
                    $("#dayEnd_1").val(ary[1]);
                } else if (strVal.split('W').length > 1) {
                    ary = strVal.split('W');
                    objRadio.eq(4).attr("checked", "checked");
                    $("#dayStart_2").val(ary[0]);
                } else if (strVal == "L") {
                    objRadio.eq(5).attr("checked", "checked");
                } else {
                    objRadio.eq(6).attr("checked", "checked");
                    ary = strVal.split(",");
                    for (var i = 0; i < ary.length; i++) {
                        $(".dayList input[value='" + ary[i] + "']").attr("checked", "checked");
                    }
                    $.fn.cronGen.tools.initCheckBox("day");
                }
            },
            initMonth: function (strVal) {
                var ary = null;
                var objRadio = $("input[name='month']");
                if (strVal == "*") {
                    objRadio.eq(0).attr("checked", "checked");
                } else if (strVal == "?") {
                    objRadio.eq(1).attr("checked", "checked");
                } else if (strVal.split('-').length > 1) {
                    ary = strVal.split('-');
                    objRadio.eq(2).attr("checked", "checked");
                    $("#monthStart_0").val(ary[0]);
                    $("#monthEnd_0").val(ary[1]);
                } else if (strVal.split('/').length > 1) {
                    ary = strVal.split('/');
                    objRadio.eq(3).attr("checked", "checked");
                    $("#monthStart_1").val(ary[0]);
                    $("#monthEnd_1").val(ary[1]);

                } else {
                    objRadio.eq(4).attr("checked", "checked");

                    ary = strVal.split(",");
                    for (var i = 0; i < ary.length; i++) {
                        $(".monthList input[value='" + ary[i] + "']").attr("checked", "checked");
                    }
                    $.fn.cronGen.tools.initCheckBox("month");
                }
            },
            initWeek: function (strVal) {
                var ary = null;
                var objRadio = $("input[name='week']");
                if (strVal == "*") {
                    objRadio.eq(0).attr("checked", "checked");
                } else if (strVal == "?") {
                    objRadio.eq(1).attr("checked", "checked");
                } else if (strVal.split('/').length > 1) {
                    ary = strVal.split('/');
                    objRadio.eq(2).attr("checked", "checked");
                    $("#weekStart_0").val(ary[0]);
                    $("#weekEnd_0").val(ary[1]);
                } else if (strVal.split('-').length > 1) {
                    ary = strVal.split('-');
                    objRadio.eq(3).attr("checked", "checked");
                    $("#weekStart_1").val(ary[0]);
                    $("#weekEnd_1").val(ary[1]);
                } else if (strVal.split('L').length > 1) {
                    ary = strVal.split('L');
                    objRadio.eq(4).attr("checked", "checked");
                    $("#weekStart_2").val(ary[0]);
                } else {
                    objRadio.eq(5).attr("checked", "checked");
                    ary = strVal.split(",");
                    for (var i = 0; i < ary.length; i++) {
                        $(".weekList input[value='" + ary[i] + "']").attr("checked", "checked");
                    }
                    $.fn.cronGen.tools.initCheckBox("week");
                }
            },
            initYear: function (strVal) {
                var ary = null;
                var objRadio = $("input[name='year']");
                if (strVal == "*") {
                    objRadio.eq(1).attr("checked", "checked");
                } else if (strVal.split('-').length > 1) {
                    ary = strVal.split('-');
                    objRadio.eq(2).attr("checked", "checked");
                    $("#yearStart_0").val(ary[0]);
                    $("#yearEnd_0").val(ary[1]);
                }
            },
            cronParse: function (cronExpress) {
                //获取参数中表达式的值
                if (cronExpress) {
                    var regs = cronExpress.split(' ');
                    $("#secondHidden").val(regs[0]);
                    $("#minHidden").val(regs[1]);
                    $("#hourHidden").val(regs[2]);
                    $("#dayHidden").val(regs[3]);
                    $("#monthHidden").val(regs[4]);
                    $("#weekHidden").val(regs[5]);

                    $.fn.cronGen.tools.initObj(regs[0], "second");
                    $.fn.cronGen.tools.initObj(regs[1], "min");
                    $.fn.cronGen.tools.initObj(regs[2], "hour");
                    $.fn.cronGen.tools.initDay(regs[3]);
                    $.fn.cronGen.tools.initMonth(regs[4]);
                    $.fn.cronGen.tools.initWeek(regs[5]);

                    if (regs.length > 6) {
                        $("#yearHidden").val(regs[6]);
                        $.fn.cronGen.tools.initYear(regs[6]);
                    }
                    form.render();
                }
            },
            cronResult: function () {
                var ids = ["secondHidden", "minHidden", "hourHidden", "dayHidden", "monthHidden", "weekHidden"];
                var tabs = ["SecondlyTab", "MinutesTab", "HourlyTab", "DailyTab", "MonthlyTab", "WeeklyTab"];
                var id = $("ul#CronGenTabs li.layui-this a").prop("id");
                var item = [];
                $.each(ids, function (i, id) {
                    item.push($("#" + id).val() === "" ? (i === 5 ? "?" : "*") : $("#" + id).val());
                });
                var currentIndex = 0;
                $.each(tabs, function (i, item) {
                    if (item === id) {
                        currentIndex = i;
                        return false;
                    }
                });
                //当前选中项之前的如果为*，则都设置成0
                for (var i = currentIndex; i >= 1; i--) {
                    if (item[i] !== "*" && item[i - 1] === "*") {
                        //item[i - 1] = "0";
                    }
                }
                //当前选中项之后的如果不为*则都设置成*
                if (item[currentIndex] === "*") {
                    for (var i = currentIndex + 1; i < item.length; i++) {
                        if (i === 5) {
                            item[i] = "?";
                        } else {
                            item[i] = "*";
                        }
                    }
                }
                return item.join(" ");
                /*var result;
                var second = $("#secondHidden").val();
                second = second== "" ? "*":second;
                $("#secondHidden").val(second);
                var minute = $("#minHidden").val();
                minute = minute== "" ? "*":minute;
                $("#minHidden").val(minute);
                var hour = $("#hourHidden").val();
                hour = hour== "" ? "*":hour;
                $("#hourHidden").val(hour);
                var day = $("#dayHidden").val();
                day = day== "" ? "*":day;
                $("#dayHidden").val(day);
                var month = $("#monthHidden").val();
                month = month== "" ? "*":month;
                $("#monthHidden").val(month);
                var week = $("#weekHidden").val();
                week = week== "" ? "?":week;
                $("#weekHidden").val(week);
                var year = $("#yearHidden").val();
                if(year!==""){
                    result = second+" "+minute+" "+hour+" "+day+" "+month+" "+week+" "+year;
                }else
                {
                    result = second+" "+minute+" "+hour+" "+day+" "+month+" "+week;
                }
                return result;*/
            },
            clearCheckbox: function (dom) {
                //清除选中的checkbox
                var list = $("." + dom + "List input").filter(":checked");//$("."+dom+"List").children().filter(":checked");
                if ($(list).length > 0) {
                    $.each(list, function (index) {
                        $(this).attr("checked", false);
                        $(this).attr("disabled", "disabled");
                        $(this).change();
                    });
                }
            },
            initCheckBox: function (dom) {
                //移除checkbox禁用
                var list = $("." + dom + "List input");//$("."+dom+"List").children();
                if ($(list).length > 0) {
                    $.each(list, function (index) {
                        $(this).removeAttr("disabled");
                    });
                }
            }
        };
    })(jQuery);
    exports("cronGen");
}).addcss('../../cron/css/cronGen.css', 'cronGencss');