/// <reference path="../Home/OrderManagerHome.ts"/>
/// <reference path="./Utility.ts"/>
var com;
(function (com) {
    var ordermanager;
    (function (ordermanager) {
        var reportingutility;
        (function (reportingutility) {
            var TransactionReports = (function () {
                function TransactionReports(layoutCell, notificationCell, reportName) {
                    this.MainLayout = layoutCell;
                    this.ReportName = reportName;
                    this.NotificationCell = notificationCell;
                    this.constructInnerLayoutForReport();
                }
                TransactionReports.prototype.constructInnerLayoutForReport = function () {
                    this.ModifiedLayoutObject = this.MainLayout.attachLayout({
                        pattern: "3U",
                        cells: [
                            { id: "a", text: "Statistics", height: 170, width: 775, header: false },
                            { id: "b", text: "Inputs", height: 170, header: false },
                            { id: "c", text: "Details", header: false }
                        ]
                    });
                    //this.contructStatisticsLayout();
                    this.constructParameterForm();
                };
                TransactionReports.prototype.setSpecificOnLoad = function () {
                    var _this = this;
                    this.ParameterForm.attachEvent("onXLE", function () {
                        _this.ParameterForm.setItemValue("ORDER_DATE=DATE", getCurrentDate());
                        _this.ReportGridParams = _this.getReportParameters();
                        _this.constructReportGrid();
                        _this.ParameterForm.attachEvent("onButtonClick", function (name) {
                            _this.ReportGridParams = _this.getReportParameters();
                            _this.ReportGrid.load("LoadReportViewGrid?gridname=" + _this.ReportName + "&ParamJson=" + _this.ReportGridParams);
                            _this.StatisticsCell.progressOn();
                            _this.StatisticsCell.attachURL("getStatistics?StatisticsName=" + _this.ReportName + "&ReportParams=" + _this.ReportGridParams);
                            _this.StatisticsCell.progressOff();
                        });
                        _this.contructStatisticsLayout();
                    });
                };
                TransactionReports.prototype.contructStatisticsLayout = function () {
                    this.StatisticsCell = this.ModifiedLayoutObject.cells("a");
                    this.StatisticsCell.fixSize(true, true);
                    this.ModifiedLayoutObject.cells("a").progressOn();
                    this.StatisticsCell.attachURL("getStatistics?StatisticsName=" + this.ReportName + "&ReportParams=" + this.getReportParameters());
                    progressOffCustom(this.ModifiedLayoutObject.cells("a"));
                };
                TransactionReports.prototype.getReportParameters = function () {
                    var params = this.ParameterForm.getValues();
                    if (this.ReportName === com.ordermanager.home.OrderManagerHome.REPORT_DAILY_ADVANCE) {
                        var date = this.ParameterForm.getItemValue("ORDER_DATE=DATE", true);
                        params["ORDER_DATE=DATE"] = date;
                        params["LOCATION=STR"] = "BAGNAN";
                    }
                    return JSON.stringify(params);
                };
                TransactionReports.prototype.constructParameterForm = function () {
                    if (this.ParameterForm != null || this.ParameterForm != undefined) {
                        this.ParameterForm.unload();
                    }
                    this.ParameterForm = this.ModifiedLayoutObject.cells("b").attachForm();
                    this.ParameterForm.load(this.ReportName + "_Form");
                    this.setSpecificOnLoad();
                };
                TransactionReports.prototype.constructReportGrid = function () {
                    var _this = this;
                    this.ModifiedLayoutObject.cells("c").progressOn();
                    this.ReportGrid = this.ModifiedLayoutObject.cells("c").attachGrid();
                    this.ReportGrid.load("LoadReportViewGrid?gridname=" + this.ReportName + "&ParamJson=" + this.ReportGridParams);
                    this.ReportGrid.attachEvent("onXLE", function () {
                        progressOffCustom(_this.ModifiedLayoutObject.cells("c"));
                    });
                };
                return TransactionReports;
            }());
            reportingutility.TransactionReports = TransactionReports;
            var DayWiseProductionReport = (function () {
                function DayWiseProductionReport(layoutCell, notificationCell) {
                    this.MainLayout = layoutCell;
                    this.NotificationCell = notificationCell;
                    this.initProductionReportLayout();
                }
                DayWiseProductionReport.prototype.initProductionReportLayout = function () {
                    this.ModifiedLayoutObject = this.MainLayout.attachLayout({
                        pattern: "3T",
                        cells: [
                            { id: "a", text: "Query", header: false, height: 100 },
                            { id: "b", text: "All Production", header: true, width: 1000 },
                            { id: "c", text: "Statistics", header: true }
                        ]
                    });
                    this.constructQueryForm();
                };
                DayWiseProductionReport.prototype.constructQueryForm = function () {
                    var _this = this;
                    this.ModifiedLayoutObject.progressOn();
                    if (this.QueryForm != null || this.QueryForm != undefined) {
                        this.QueryForm.unload();
                    }
                    this.QueryForm = this.ModifiedLayoutObject.cells("a").attachForm();
                    this.QueryForm.load("daily_productionReport_Form");
                    this.QueryForm.attachEvent("onEnter", function () {
                        // var ParameterValue = this.QueryForm.getValues();
                        // this.loadAssignmentGridData(ParameterValue);
                    });
                    this.QueryForm.attachEvent("onXLE", function () {
                        _this.QueryForm.setFocusOnFirstActive();
                        // this.setSpecificOnLoad();
                        progressOffCustom(_this.ModifiedLayoutObject);
                    });
                    this.QueryForm.attachEvent("onChange", function (name, value) {
                        if (name == "TYPE=STR") {
                            _this.ModifiedLayoutObject.progressOn();
                            com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.QueryForm.getOptions("NAME=STR"), "EMPLOYEES", "EMP_NAME", "EMP_ROLE", value);
                            progressOffCustom(_this.ModifiedLayoutObject);
                        }
                        if (name == "REPORT_TYPE=STR") {
                            if (value == "BY DATE") {
                                _this.QueryForm.enableItem("FROM_DATE=DATE");
                                _this.QueryForm.enableItem("TO_DATE=DATE");
                            }
                            else {
                                _this.QueryForm.disableItem("FROM_DATE=DATE");
                                _this.QueryForm.disableItem("TO_DATE=DATE");
                            }
                        }
                    });
                    this.QueryForm.attachEvent("onButtonClick", function (name) {
                        var ProductionType = "TO_" + _this.QueryForm.getItemValue("TYPE=STR", true);
                        var Name = _this.QueryForm.getItemValue("NAME=STR", true);
                        var ReportType = _this.QueryForm.getItemValue("REPORT_TYPE=STR", true);
                        var fromDate = "";
                        var toDate = "";
                        if (ReportType == "BY DATE") {
                            fromDate = _this.QueryForm.getItemValue("FROM_DATE=DATE", true);
                            toDate = _this.QueryForm.getItemValue("TO_DATE=DATE", true);
                        }
                        if (name == "getreport") {
                            _this.counstructProductionGrid(ProductionType, Name, ReportType, fromDate, toDate);
                            _this.statisticCounterConstruction(ProductionType, Name, ReportType, fromDate, toDate);
                        }
                        else {
                            _this.counstructProductionGrid(ProductionType, Name, ReportType, fromDate, toDate);
                            _this.statisticCounterConstruction(ProductionType, Name, ReportType, fromDate, toDate);
                            _this.ReportGrid.attachEvent("onXLE", function () {
                                progressOffCustom(_this.ModifiedLayoutObject.cells("b"));
                                _this.ReportGrid.printView("<div> <div style='background-color:#e5e5e5;font-size:24px;display:table;font-weight:bold;'> MALLICK DRESSES </div> <div style='font-size:18px;float:left;font-weight:bold;'> ORDER ASSIGNMENT REPORT<br> NAME : " + Name + "<br> DATE : 31/01/2017 </div> </div>", "<div></div>");
                            });
                        }
                    });
                };
                DayWiseProductionReport.prototype.getReportParameters = function () {
                    var params = this.QueryForm.getValues();
                    if (this.ReportName === com.ordermanager.home.OrderManagerHome.REPORT_DAILY_ADVANCE) {
                        var date = this.QueryForm.getItemValue("ORDER_DATE=DATE", true);
                        params["ORDER_DATE=DATE"] = date;
                        params["LOCATION=STR"] = "BAGNAN";
                    }
                    return JSON.stringify(params);
                };
                DayWiseProductionReport.prototype.counstructProductionGrid = function (ProductionType, Name, ReportType, fromDate, toDate) {
                    var _this = this;
                    this.ModifiedLayoutObject.cells("b").progressOn();
                    this.ReportGrid = this.ModifiedLayoutObject.cells("b").attachGrid();
                    this.ReportGrid.enableColSpan(true);
                    this.ReportGrid.enableMultiline(true);
                    this.ReportGrid.load('getDayWiseProductionDetails2?ProductionType=' + ProductionType + '&Name=' + Name + '&ReportType=' + ReportType + "&fromDate=" + fromDate + "&toDate=" + toDate);
                    this.ReportGrid.attachEvent("onXLE", function () {
                        progressOffCustom(_this.ModifiedLayoutObject.cells("b"));
                    });
                };
                DayWiseProductionReport.prototype.statisticCounterConstruction = function (ProductionType, Name, ReportType, fromDate, toDate) {
                    var statsData = SynchronousGetAjaxRequest('getDayWiseProductionStats?ProductionType=' + ProductionType + '&Name=' + Name + '&ReportType=' + ReportType + "&fromDate=" + fromDate + "&toDate=" + toDate);
                    var AllExtraItem = statsData.RESPONSE_VALUE.ALL_ITEMS;
                    delete statsData.RESPONSE_VALUE.ALL_ITEMS;
                    var AllValues = statsData.RESPONSE_VALUE;
                    var StatFields = AllValues;
                    this.StatisticsGrid = this.ModifiedLayoutObject.cells("c").attachGrid();
                    this.StatisticsGrid.setHeader("Stats");
                    this.StatisticsGrid.setNoHeader(true);
                    this.StatisticsGrid.setColAlign("left");
                    this.StatisticsGrid.setColTypes("ro");
                    this.StatisticsGrid.init();
                    for (var SingleField in StatFields) {
                        this.StatisticsGrid.addRow("STAT_KEY_NAME_" + SingleField, SingleField);
                        this.StatisticsGrid.addRow("STAT_VALUE_NAME_" + StatFields[SingleField], StatFields[SingleField]);
                        this.StatisticsGrid.setRowTextStyle("STAT_KEY_NAME_" + SingleField, "color:white;background-color: #00a0ea; font-weight:bold; ");
                        this.StatisticsGrid.setRowTextStyle("STAT_VALUE_NAME_" + StatFields[SingleField], "background-color:#ebffbc; font-weight:bold;font-size:18px;font-family:Impact;");
                    }
                    var StatFields = AllExtraItem;
                    for (var SingleField in StatFields) {
                        this.StatisticsGrid.addRow("STAT_KEY_NAME_" + SingleField, SingleField);
                        this.StatisticsGrid.addRow("STAT_VALUE_NAME_" + StatFields[SingleField], StatFields[SingleField]);
                        this.StatisticsGrid.setRowTextStyle("STAT_KEY_NAME_" + SingleField, "color:white;background-color: #00a0ea; font-weight:bold; ");
                        this.StatisticsGrid.setRowTextStyle("STAT_VALUE_NAME_" + StatFields[SingleField], "background-color:#ebffbc; font-weight:bold;font-size:18px;font-family:Impact;");
                    }
                };
                return DayWiseProductionReport;
            }());
            reportingutility.DayWiseProductionReport = DayWiseProductionReport;
        })(reportingutility = ordermanager.reportingutility || (ordermanager.reportingutility = {}));
    })(ordermanager = com.ordermanager || (com.ordermanager = {}));
})(com || (com = {}));
