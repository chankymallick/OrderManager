/// <reference path="../Home/OrderManagerHome.ts"/>
/// <reference path="./Utility.ts"/>
var com;
(function (com) {
    var ordermanager;
    (function (ordermanager) {
        var reportingutility;
        (function (reportingutility) {
            var TransactionReports = /** @class */ (function () {
                function TransactionReports(layoutCell, notificationCell, reportName) {
                    this.MainLayout = layoutCell;
                    this.ReportName = reportName;
                    this.NotificationCell = notificationCell;
                    this.constructInnerLayoutForReport();
                    ReportGrid = this.ReportGrid();
                }
                TransactionReports.prototype.constructInnerLayoutForReport = function () {
                    var _this = this;
                    this.ModifiedLayoutObject = this.MainLayout.attachLayout({
                        pattern: "3U",
                        cells: [
                            { id: "a", text: "Statistics", height: 170, width: 775, header: false },
                            { id: "b", text: "Inputs", height: 170, header: false },
                            { id: "c", text: "Details", header: false }
                        ]
                    });
                    this.OperationToolbar = this.ModifiedLayoutObject.attachToolbar();
                    this.OperationToolbar.loadStruct("reportToolbar?formname=" + this.ReportName, "json");
                    this.OperationToolbar.attachEvent("onClick", function (id) {
                        if (id === "print") {
                            _this.printReport();
                        }
                        if (id === "refresh") {
                        }
                    });
                    //this.contructStatisticsLayout();
                    this.constructParameterForm();
                    shortcut.add("F2", function () {
                        _this.printReport();
                    }, {
                        'type': 'keyup',
                        'disable_in_input': false,
                        'target': document,
                        'propagate': true
                    });
                    shortcut.add("F4", function () {
                    }, {
                        'type': 'keyup',
                        'disable_in_input': false,
                        'target': document,
                        'propagate': true
                    });
                };
                TransactionReports.prototype.printReport = function () {
                    if (this.ReportName === com.ordermanager.home.OrderManagerHome.REPORT_DAILY_ADVANCE) {
                        this.ReportDisplayName = "ORDER ADVANCE REPORT";
                    }
                    else if (this.ReportName === com.ordermanager.home.OrderManagerHome.REPORT_DELIVERY_TRANSACTIONS) {
                        this.ReportDisplayName = "DELIVERY PAYMENTS REPORT";
                    }
                    this.ReportGrid.detachHeader(1);
                    var StatsHtmlText = "<table>";
                    for (var StatValue in this.StatsJSON) {
                        StatsHtmlText += "<tr><td style='font-weight:bold;text-align:left;'>" + this.StatsJSON[StatValue][1] + "<td/><td style='font-weight:bold;text-align:left;'>: " + this.StatsJSON[StatValue][2] + "</td></tr>";
                    }
                    StatsHtmlText += "</table>";
                    this.ReportGrid.printView("<div> <div style='background-color:#e5e5e5;font-size:24px;font-weight:bold;'> MALLICK DRESSES </div> <div style='font-size:18px;font-weight:bold;'> " + this.ReportDisplayName + " <br>" + StatsHtmlText + " </div> </div>", "<div></div>");
                };
                TransactionReports.prototype.returnWhiteSpace = function (numberofspace) {
                    var spaces = "";
                    for (var i = 0; i <= 20 - numberofspace; i++) {
                        spaces += "&nbsp;";
                    }
                    return spaces;
                };
                TransactionReports.prototype.setSpecificOnLoad = function () {
                    var _this = this;
                    this.ParameterForm.attachEvent("onXLE", function () {
                        _this.ParameterForm.setItemValue("REPORT_DATE=DATE", getCurrentDate());
                        _this.ReportGridParams = _this.getReportParameters();
                        _this.constructReportGrid();
                        _this.ParameterForm.attachEvent("onButtonClick", function (name) {
                            _this.ReportGridParams = _this.getReportParameters();
                            _this.ReportGrid.load("LoadReportViewGrid?gridname=" + _this.ReportName + "&ParamJson=" + _this.ReportGridParams);
                            _this.StatisticsCell.progressOn();
                            var Response = SynchronousGetAjaxRequest("getStatisticsJSON?StatisticsName=" + _this.ReportName + "&ReportParams=" + _this.getReportParameters(), null);
                            if (Response != null || Response != undefined) {
                                _this.StatsJSON = Response.STAT_VALUES;
                            }
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
                    var Response = SynchronousGetAjaxRequest("getStatisticsJSON?StatisticsName=" + this.ReportName + "&ReportParams=" + this.getReportParameters(), null);
                    if (Response != null || Response != undefined) {
                        this.StatsJSON = Response.STAT_VALUES;
                    }
                    this.StatisticsCell.attachURL("getStatistics?StatisticsName=" + this.ReportName + "&ReportParams=" + this.getReportParameters());
                    progressOffCustom(this.ModifiedLayoutObject.cells("a"));
                };
                TransactionReports.prototype.getReportParameters = function () {
                    var params = this.ParameterForm.getValues();
                    // if (this.ReportName === com.ordermanager.home.OrderManagerHome.REPORT_DAILY_ADVANCE) {
                    // }
                    var date = this.ParameterForm.getItemValue("REPORT_DATE=DATE", true);
                    params["REPORT_DATE=DATE"] = date;
                    params["LOCATION=STR"] = "BAGNAN";
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
            var DayWiseProductionReport = /** @class */ (function () {
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
                        _this.MainStatsForPrint = "NAME : " + Name + "<br>" + "TYPE : " + ReportType;
                        if (ReportType == "BY DATE") {
                            fromDate = _this.QueryForm.getItemValue("FROM_DATE=DATE", true);
                            toDate = _this.QueryForm.getItemValue("TO_DATE=DATE", true);
                            _this.MainStatsForPrint += ", FROM :" + fromDate + ", TO :" + toDate;
                        }
                        _this.MainStatsForPrint += "<br>";
                        if (name == "getreport") {
                            _this.counstructProductionGrid(ProductionType, Name, ReportType, fromDate, toDate);
                            _this.statisticCounterConstruction(ProductionType, Name, ReportType, fromDate, toDate);
                        }
                        else {
                            _this.counstructProductionGrid(ProductionType, Name, ReportType, fromDate, toDate);
                            _this.statisticCounterConstruction(ProductionType, Name, ReportType, fromDate, toDate);
                            _this.ReportGrid.attachEvent("onXLE", function () {
                                progressOffCustom(_this.ModifiedLayoutObject.cells("b"));
                                _this.ReportGrid.printView("<div> <div style='background-color:#e5e5e5;font-size:24px;display:table;font-weight:bold;'> MALLICK DRESSES </div> <div style='font-size:18px;float:left;font-weight:bold;'> ORDER ASSIGNMENT REPORT <br>" + _this.MainStatsForPrint + " </div> </div>", "<div></div>");
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
                    this.ReportGrid.setStyle("background-color:#003eba;color:white; font-weight:bold;", "font-weight:bold;", "", "");
                    this.ReportGrid.enableColSpan(true);
                    this.ReportGrid.enableMultiline(true);
                    this.ReportGrid.load('getDayWiseProductionDetails2?ProductionType=' + ProductionType + '&Name=' + Name + '&ReportType=' + ReportType + "&fromDate=" + fromDate + "&toDate=" + toDate);
                    this.ReportGrid.attachEvent("onXLE", function () {
                        progressOffCustom(_this.ModifiedLayoutObject.cells("b"));
                    });
                };
                DayWiseProductionReport.prototype.statisticCounterConstruction = function (ProductionType, Name, ReportType, fromDate, toDate) {
                    this.ModifiedLayoutObject.cells("c").progressOn();
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
                        this.StatisticsGrid.addRow("STAT_KEY_NAME_" + SingleField, SingleField.replace("_", " "));
                        this.StatisticsGrid.addRow("STAT_VALUE_NAME_" + StatFields[SingleField], StatFields[SingleField]);
                        this.StatisticsGrid.setRowTextStyle("STAT_KEY_NAME_" + SingleField, "color:white;background-color: " + com.ordermanager.utilty.MainUtility.getRandomColorDark() + ";font-weight:bold;font-size:20px ");
                        this.StatisticsGrid.setRowTextStyle("STAT_VALUE_NAME_" + StatFields[SingleField], "background-color:#ebffbc; font-weight:bold;font-size:22px;");
                        this.MainStatsForPrint += SingleField.replace("_", " ") + " : " + StatFields[SingleField] + ", ";
                    }
                    this.MainStatsForPrint += "<br>";
                    var StatFields = AllExtraItem;
                    for (var SingleField in StatFields) {
                        this.StatisticsGrid.addRow("STAT_KEY_NAME_" + SingleField, SingleField.replace("_", " "));
                        this.StatisticsGrid.addRow("STAT_VALUE_NAME_" + StatFields[SingleField], StatFields[SingleField]);
                        this.StatisticsGrid.setRowTextStyle("STAT_KEY_NAME_" + SingleField, "color:white;background-color: " + com.ordermanager.utilty.MainUtility.getRandomColorDark() + "; font-weight:bold;font-size:20px ");
                        this.StatisticsGrid.setRowTextStyle("STAT_VALUE_NAME_" + StatFields[SingleField], "background-color:#ebffbc; font-weight:bold;font-size:22px;");
                        this.MainStatsForPrint += SingleField.replace("_", " ") + " : " + StatFields[SingleField] + " ,";
                    }
                    progressOffCustom(this.ModifiedLayoutObject.cells("c"));
                };
                return DayWiseProductionReport;
            }());
            reportingutility.DayWiseProductionReport = DayWiseProductionReport;
            var WagePaymentSystem = /** @class */ (function () {
                function WagePaymentSystem(layoutCell, notificationCell) {
                    this.removedBills = [];
                    this.ParameterJSON = {};
                    this.MainLayout = layoutCell;
                    this.NotificationCell = notificationCell;
                    this.initProductionReportLayout();
                }
                WagePaymentSystem.prototype.initProductionReportLayout = function () {
                    var _this = this;
                    this.ModifiedLayoutObject = this.MainLayout.attachLayout({
                        pattern: "3T",
                        cells: [
                            { id: "a", text: "Query", header: false, height: 100 },
                            { id: "b", text: "All Production", header: true, width: 1000 },
                            { id: "c", text: "Statistics", header: true }
                        ]
                    });
                    this.constructQueryForm();
                    this.constructToolBar();
                    shortcut.add("F8", function () {
                        _this.initiateWagePaymentProcess();
                    }, {
                        'type': 'keyup',
                        'disable_in_input': false,
                        'target': document,
                        'propagate': true
                    });
                    shortcut.add("F4", function () {
                        _this.refreshPaymentModule();
                    }, {
                        'type': 'keyup',
                        'disable_in_input': false,
                        'target': document,
                        'propagate': true
                    });
                    shortcut.add("F9", function () {
                        _this.openPaymentPrintingModule("MANUAL");
                    }, {
                        'type': 'keyup',
                        'disable_in_input': false,
                        'target': document,
                        'propagate': true
                    });
                };
                WagePaymentSystem.prototype.openPaymentPrintingModule = function (type) {
                    new com.ordermanager.reportingutility.PaymentPrintView(type, "", "", getCurrentDate());
                };
                WagePaymentSystem.prototype.constructToolBar = function () {
                    var _this = this;
                    this.OperationToolbar = this.ModifiedLayoutObject.attachToolbar();
                    this.OperationToolbar.loadStruct("wagePaymentUnpaidToolbar?formname=wagePayment", "json");
                    this.OperationToolbar.attachEvent("onClick", function (id) {
                        if (id === "pay") {
                            _this.initiateWagePaymentProcess();
                        }
                        if (id === "print") {
                            new com.ordermanager.reportingutility.PaymentPrintView("MANUAL", "", "", "");
                        }
                    });
                };
                WagePaymentSystem.prototype.constructQueryForm = function () {
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
                        _this.reportEventActions(name);
                    });
                };
                WagePaymentSystem.prototype.refreshPaymentModule = function () {
                    this.ReportName = "";
                    this.StatisticsCell = null;
                    this.StatisticsGrid = null;
                    this.QueryForm = null;
                    this.ReportGrid = null;
                    this.ReportGridParams = null;
                    this.MainStatsForPrint = null;
                    this.ExtraStatsForPrint = null;
                    this.DataTabber = null;
                    this.DeletedOrderGrid = null;
                    this.removedBills = [];
                    this.ParameterJSON = {};
                    this.initProductionReportLayout();
                    this.NotificationCell.collapse();
                };
                WagePaymentSystem.prototype.reportEventActions = function (name) {
                    var _this = this;
                    var ProductionType = "TO_" + this.QueryForm.getItemValue("TYPE=STR", true);
                    var Name = this.QueryForm.getItemValue("NAME=STR", true);
                    var ReportType = this.QueryForm.getItemValue("REPORT_TYPE=STR", true);
                    var fromDate = "";
                    var toDate = "";
                    this.MainStatsForPrint = "NAME : " + Name + "<br>" + "TYPE : " + ReportType;
                    if (ReportType == "BY DATE") {
                        fromDate = this.QueryForm.getItemValue("FROM_DATE=DATE", true);
                        toDate = this.QueryForm.getItemValue("TO_DATE=DATE", true);
                        this.MainStatsForPrint += ", FROM :" + fromDate + ", TO :" + toDate;
                    }
                    this.MainStatsForPrint += "<br>";
                    if (name == "getreport") {
                        this.counstructProductionGrid(ProductionType, Name, ReportType, fromDate, toDate);
                        this.statisticCounterConstruction(ProductionType, Name, ReportType, fromDate, toDate);
                    }
                    else if (name == "refreshCounter") {
                        this.statisticCounterConstruction(ProductionType, Name, ReportType, fromDate, toDate);
                    }
                    else {
                        //this.counstructProductionGrid(ProductionType, Name, ReportType, fromDate, toDate);
                        this.statisticCounterConstruction(ProductionType, Name, ReportType, fromDate, toDate);
                        this.ReportGrid.attachEvent("onXLE", function () {
                            progressOffCustom(_this.ModifiedLayoutObject.cells("b"));
                            _this.ReportGrid.printView("<div> <div style='background-color:#e5e5e5;font-size:24px;display:table;font-weight:bold;'> MALLICK DRESSES </div> <div style='font-size:18px;float:left;font-weight:bold;'> ORDER ASSIGNMENT REPORT <br>" + _this.MainStatsForPrint + " </div> </div>", "<div></div>");
                        });
                    }
                };
                WagePaymentSystem.prototype.getReportParameters = function () {
                    var params = this.QueryForm.getValues();
                    if (this.ReportName === com.ordermanager.home.OrderManagerHome.REPORT_DAILY_ADVANCE) {
                        var date = this.QueryForm.getItemValue("ORDER_DATE=DATE", true);
                        params["ORDER_DATE=DATE"] = date;
                        params["LOCATION=STR"] = "BAGNAN";
                    }
                    return JSON.stringify(params);
                };
                WagePaymentSystem.prototype.counstructProductionGrid = function (ProductionType, Name, ReportType, fromDate, toDate) {
                    var _this = this;
                    this.ModifiedLayoutObject.cells("b").progressOn();
                    this.removedBills = [];
                    this.DataTabber = this.ModifiedLayoutObject.cells("b").attachTabbar();
                    this.DataTabber.addTab("AllItems", "All Orders", null, null, true, false);
                    this.DataTabber.addTab("DeletedItems", "Deleted Orders", null, null, false, false);
                    this.ReportGrid = this.DataTabber.tabs("AllItems").attachGrid();
                    this.ReportGrid.setStyle("background-color:#003eba;color:white; font-weight:bold;", "font-weight:bold;", "", "");
                    this.ReportGrid.enableColSpan(true);
                    this.ReportGrid.enableMultiline(true);
                    this.ReportGrid.load('getDayWiseProductionDetailsUnpaidWage?ProductionType=' + ProductionType + '&Name=' + Name + '&ReportType=' + ReportType + "&fromDate=" + fromDate + "&toDate=" + toDate);
                    this.ReportGrid.attachEvent("onXLE", function () {
                        progressOffCustom(_this.ModifiedLayoutObject.cells("b"));
                    });
                    this.DeletedOrderGrid = this.DataTabber.tabs("DeletedItems").attachGrid();
                    this.DeletedOrderGrid.setStyle("background-color:#003eba;color:white; font-weight:bold;", "font-weight:bold;", "", "");
                    this.DeletedOrderGrid.setHeader("NO,BILL,MAIN_STATUS,LOCATION,WAGE,WAGE_STATUS,ITEMS,QTY,ORDER_TYPE,VENDOR,REMOVE");
                    this.DeletedOrderGrid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
                    this.DeletedOrderGrid.setColAlign("center,center,center,center,center,center,center,center,center,center,center");
                    this.DeletedOrderGrid.init();
                    this.ReportGrid.attachEvent("onRowSelect", function (id, index) {
                        var BillNo = _this.ReportGrid.cells(id, 1).getValue();
                        dhtmlx.confirm({
                            type: "confirm",
                            text: "Do you want to remove Bill Number " + BillNo + " ?",
                            callback: function (result) {
                                if (result) {
                                    var SourceRowId = id;
                                    var TargetRowId = _this.DeletedOrderGrid.getRowId(_this.DeletedOrderGrid.getRowsNum());
                                    _this.moveOrderGrid(SourceRowId, "", _this.ReportGrid, _this.DeletedOrderGrid);
                                    showSuccessNotification(BillNo + " removed from List");
                                }
                            }
                        });
                    });
                    this.DeletedOrderGrid.attachEvent("onRowAdded", function (rId) {
                        _this.removedBills = [];
                        var index = 1;
                        _this.DeletedOrderGrid.forEachRow(function (id) {
                            _this.DeletedOrderGrid.cells(id, 0).setValue(index);
                            _this.removedBills.push(_this.DeletedOrderGrid.cells(id, 1).getValue().toString());
                            index += 1;
                        });
                        _this.DataTabber.tabs("DeletedItems").setText("Deleted Orders (" + (index - 1) + ")");
                    });
                };
                WagePaymentSystem.prototype.getRemovedBills = function () {
                    return "'" + this.removedBills.join("','") + "'";
                };
                WagePaymentSystem.prototype.moveOrderGrid = function (SourceRowID, TargetRowID, SourceGridObject, TargetGridObject) {
                    this.ModifiedLayoutObject.progressOn();
                    this.ReportGrid.moveRowTo(SourceRowID, null, "move", "sibling", SourceGridObject, TargetGridObject);
                    this.reportEventActions("refreshCounter");
                    progressOffCustom(this.ModifiedLayoutObject);
                };
                WagePaymentSystem.prototype.statisticCounterConstruction = function (ProductionType, Name, ReportType, fromDate, toDate) {
                    this.ModifiedLayoutObject.cells("c").progressOn();
                    var statsData = SynchronousGetAjaxRequest('getDayWiseProductionStatsUnpaid?ProductionType=' + ProductionType + '&Name=' + Name + '&ReportType=' + ReportType + "&fromDate=" + fromDate + "&toDate=" + toDate + "&removedBills=" + this.getRemovedBills());
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
                        this.StatisticsGrid.addRow("STAT_KEY_NAME_" + SingleField, SingleField.replace("_", " "));
                        this.StatisticsGrid.addRow("STAT_VALUE_NAME_" + StatFields[SingleField], StatFields[SingleField]);
                        this.StatisticsGrid.setRowTextStyle("STAT_KEY_NAME_" + SingleField, "color:white;background-color: " + com.ordermanager.utilty.MainUtility.getRandomColorDark() + ";font-weight:bold;font-size:20px ");
                        this.StatisticsGrid.setRowTextStyle("STAT_VALUE_NAME_" + StatFields[SingleField], "background-color:#ebffbc; font-weight:bold;font-size:22px;");
                        this.MainStatsForPrint += SingleField.replace("_", " ") + " : " + StatFields[SingleField] + ", ";
                    }
                    this.MainStatsForPrint += "<br>";
                    var StatFields = AllExtraItem;
                    for (var SingleField in StatFields) {
                        this.StatisticsGrid.addRow("STAT_KEY_NAME_" + SingleField, SingleField.replace("_", " "));
                        this.StatisticsGrid.addRow("STAT_VALUE_NAME_" + StatFields[SingleField], StatFields[SingleField]);
                        this.StatisticsGrid.setRowTextStyle("STAT_KEY_NAME_" + SingleField, "color:white;background-color: " + com.ordermanager.utilty.MainUtility.getRandomColorDark() + "; font-weight:bold;font-size:20px ");
                        this.StatisticsGrid.setRowTextStyle("STAT_VALUE_NAME_" + StatFields[SingleField], "background-color:#ebffbc; font-weight:bold;font-size:22px;");
                        this.MainStatsForPrint += SingleField.replace("_", " ") + " : " + StatFields[SingleField] + " ,";
                    }
                    progressOffCustom(this.ModifiedLayoutObject.cells("c"));
                };
                WagePaymentSystem.prototype.initiateWagePaymentProcess = function () {
                    var _this = this;
                    this.ModifiedLayoutObject.progressOn();
                    var ProductionType = "TO_" + this.QueryForm.getItemValue("TYPE=STR", true);
                    var Name = this.QueryForm.getItemValue("NAME=STR", true);
                    var AllBillNos = [];
                    this.ReportGrid.forEachRow(function (id) {
                        var Data = _this.ReportGrid.cells(id, 1).getValue();
                        if (Data.indexOf("/") < 0) {
                            AllBillNos.push(Data);
                        }
                    });
                    this.ParameterJSON["ALL_BILL_NO"] = AllBillNos;
                    var Response = SynchronousGetAjaxRequest("wagePayment?ProductionType=" + ProductionType + "&Name=" + Name + "&BILLS=" + JSON.stringify(this.ParameterJSON));
                    if (Response.RESPONSE_STATUS === "SUCCESS") {
                        this.ReportGrid.forEachRow(function (id) {
                            var BILLNO = _this.ReportGrid.cells(id, 1).getValue();
                            if (BILLNO.indexOf("/") < 0) {
                                _this.ReportGrid.setUserData(id, "SERVER_DATA", Response.RESPONSE_VALUE[BILLNO]);
                                if (Response.RESPONSE_VALUE[BILLNO].split(",")[0].indexOf("SUCCES") == 0) {
                                    _this.ReportGrid.cells(id, 10).setValue("");
                                    _this.ReportGrid.setRowTextStyle(id, "color:#0026ff;background-color: #cccccc; font-weight:bold; ");
                                    _this.ReportGrid.cells(id, 10).setValue("<img height='23px' width='20px' src='resources/Images/success.png'/>");
                                }
                                else {
                                    _this.ReportGrid.cells(id, 10).setValue("<a><img height='20px' width='20px' src='resources/Images/failed.png'/></a>");
                                }
                            }
                        });
                        this.OperationToolbar.enableItem("new");
                        progressOffCustom(this.ModifiedLayoutObject);
                        showSuccessNotificationWithICON(Response.RESPONSE_MESSAGE);
                        this.NotificationCell.attachHTMLString(" <b style='color:red'>" + Response.RESPONSE_MESSAGE + "</b><br>" + JSON.stringify(Response.RESPONSE_VALUE));
                        this.NotificationCell.expand();
                        new com.ordermanager.reportingutility.PaymentPrintView("DIRECT", ProductionType, Name, getCurrentDate());
                    }
                    else {
                        showFailedNotificationWithICON(Response.RESPONSE_MESSAGE);
                        this.NotificationCell.attachHTMLString(" <b style='color:red'>" + Response.RESPONSE_VALUE.EXCEPTION_MESSAGE + "</b>");
                        this.NotificationCell.expand();
                    }
                    progressOffCustom(this.ModifiedLayoutObject);
                };
                return WagePaymentSystem;
            }());
            reportingutility.WagePaymentSystem = WagePaymentSystem;
            var PaymentPrintView = /** @class */ (function () {
                function PaymentPrintView(PrintType, ProductionType, Name, paymentDate) {
                    var _this = this;
                    var height = $(window).height();
                    this.PRINT_TYPE = PrintType;
                    this.MainWindow = com.ordermanager.utilty.MainUtility.getModelWindow("Payment Printing Module", 1200, height - 50);
                    this.MainWindow.show();
                    this.ModifiedLayoutObject = this.MainWindow.attachLayout({
                        pattern: "3T",
                        cells: [
                            { id: "a", text: "Query", header: false, width: 900, height: 80 },
                            { id: "b", text: "All Production", header: false, width: 900 },
                            { id: "c", text: "Stats", header: false },
                        ]
                    });
                    this.OperationToolbar = this.ModifiedLayoutObject.attachToolbar();
                    this.OperationToolbar.loadStruct("printModuleToolbar?formname=printModule", "json");
                    this.OperationToolbar.attachEvent("onClick", function (id) {
                        if (id === "new_search") {
                            _this.refreshPrintingmodule();
                        }
                        if (id === "close") {
                            _this.MainWindow.close();
                            _this.MainWindow.unload();
                        }
                    });
                    this.constructQueryForm();
                    if (PrintType == "DIRECT") {
                        this.MainStatsForPrint = "NAME : " + Name + "<br>";
                        this.counstructProductionGrid(ProductionType, Name, paymentDate);
                        this.statisticCounterConstruction(ProductionType, Name, paymentDate);
                        this.ReportGrid.attachEvent("onXLE", function () {
                            _this.ReportGrid.printView("<div> <div style='background-color:#e5e5e5;font-size:24px;display:table;font-weight:bold;'> MALLICK DRESSES </div> <div style='font-size:18px;float:left;font-weight:bold;'> WAGE PAYMENT RECEIPT <br> <div style='font-size:24px;display:table;font-weight:bold;'>TOTAL PAYMENT : " + _this.TOTAL_WAGE + "/-</div> " + _this.MainStatsForPrint + " </div> </div>", "<div></div>");
                        });
                    }
                    shortcut.add("F2", function () {
                        _this.refreshPrintingmodule();
                    }, {
                        'type': 'keyup',
                        'disable_in_input': false,
                        'target': document,
                        'propagate': true
                    });
                    shortcut.add("ESC", function () {
                        _this.MainWindow.close();
                        _this.MainWindow.unload();
                    }, {
                        'type': 'keyup',
                        'disable_in_input': false,
                        'target': document,
                        'propagate': true
                    });
                }
                PaymentPrintView.prototype.refreshPrintingmodule = function () {
                    this.constructQueryForm();
                    this.ReportGrid.destructor();
                    this.StatisticsGrid.destructor();
                    this.TOTAL_WAGE = 0;
                    this.MainStatsForPrint = "";
                    this.PRINT_TYPE = "";
                };
                PaymentPrintView.prototype.constructQueryForm = function () {
                    var _this = this;
                    this.ModifiedLayoutObject.progressOn();
                    if (this.QueryForm != null || this.QueryForm != undefined) {
                        this.QueryForm.unload();
                    }
                    this.QueryForm = this.ModifiedLayoutObject.cells("a").attachForm();
                    this.QueryForm.load("print_module_form");
                    this.QueryForm.attachEvent("onEnter", function () {
                    });
                    this.QueryForm.attachEvent("onXLE", function () {
                        _this.QueryForm.setFocusOnFirstActive();
                        progressOffCustom(_this.ModifiedLayoutObject);
                    });
                    this.QueryForm.attachEvent("onChange", function (name, value) {
                        if (name == "TYPE=STR") {
                            _this.ModifiedLayoutObject.progressOn();
                            com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(_this.QueryForm.getOptions("NAME=STR"), "EMPLOYEES", "EMP_NAME", "EMP_ROLE", value);
                            progressOffCustom(_this.ModifiedLayoutObject);
                        }
                    });
                    this.QueryForm.attachEvent("onButtonClick", function (name) {
                        _this.printData();
                    });
                };
                PaymentPrintView.prototype.printData = function () {
                    var _this = this;
                    var ProductionType = "TO_" + this.QueryForm.getItemValue("TYPE=STR", true);
                    var Name = this.QueryForm.getItemValue("NAME=STR", true);
                    var paymentDate = this.QueryForm.getItemValue("PAY_DATE=DATE", true);
                    this.MainStatsForPrint = "NAME : " + Name + "<br>";
                    this.counstructProductionGrid(ProductionType, Name, paymentDate);
                    this.statisticCounterConstruction(ProductionType, Name, paymentDate);
                    if (name == "printReport" || this.PRINT_TYPE === "DIRECT") {
                        this.ReportGrid.attachEvent("onXLE", function () {
                            _this.ReportGrid.printView("<div> <div style='background-color:#e5e5e5;font-size:24px;display:table;font-weight:bold;'> MALLICK DRESSES </div> <div style='font-size:18px;float:left;font-weight:bold;'> WAGE PAYMENT RECEIPT <br> <div style='font-size:24px;display:table;font-weight:bold;'>TOTAL PAYMENT : " + _this.TOTAL_WAGE + "/-</div> " + _this.MainStatsForPrint + " </div> </div>", "<div></div>");
                        });
                    }
                };
                PaymentPrintView.prototype.counstructProductionGrid = function (ProductionType, Name, PaymentDate) {
                    var _this = this;
                    this.ModifiedLayoutObject.progressOn();
                    this.ReportGrid = this.ModifiedLayoutObject.cells("b").attachGrid();
                    this.ReportGrid.setStyle("background-color:#003eba;color:white; font-weight:bold;", "font-weight:bold;", "", "");
                    this.ReportGrid.enableColSpan(true);
                    this.ReportGrid.enableMultiline(true);
                    this.ReportGrid.load('getDayWisePaidWagePaymentOrders?ProductionType=' + ProductionType + '&Name=' + Name + '&PaymentDate=' + PaymentDate);
                    this.ReportGrid.attachEvent("onXLE", function () {
                        progressOffCustom(_this.ModifiedLayoutObject);
                    });
                };
                PaymentPrintView.prototype.statisticCounterConstruction = function (ProductionType, Name, PaymentDate) {
                    this.ModifiedLayoutObject.cells("c").progressOn();
                    var statsData = SynchronousGetAjaxRequest('getDayWisePaidWagePaymentStats?ProductionType=' + ProductionType + '&Name=' + Name + '&PaymentDate=' + PaymentDate);
                    var AllExtraItem = statsData.RESPONSE_VALUE.ALL_ITEMS;
                    delete statsData.RESPONSE_VALUE.ALL_ITEMS;
                    var AllValues = statsData.RESPONSE_VALUE;
                    this.TOTAL_WAGE = AllValues.TOTAL_WAGE;
                    var StatFields = AllValues;
                    this.StatisticsGrid = this.ModifiedLayoutObject.cells("c").attachGrid();
                    this.StatisticsGrid.setHeader("Stats");
                    this.StatisticsGrid.setNoHeader(true);
                    this.StatisticsGrid.setColAlign("left");
                    this.StatisticsGrid.setColTypes("ro");
                    this.StatisticsGrid.init();
                    for (var SingleField in StatFields) {
                        this.StatisticsGrid.addRow("STAT_KEY_NAME_" + SingleField, SingleField.replace("_", " "));
                        this.StatisticsGrid.addRow("STAT_VALUE_NAME_" + StatFields[SingleField], StatFields[SingleField]);
                        this.StatisticsGrid.setRowTextStyle("STAT_KEY_NAME_" + SingleField, "color:white;background-color: " + com.ordermanager.utilty.MainUtility.getRandomColorDark() + ";font-weight:bold;font-size:20px ");
                        this.StatisticsGrid.setRowTextStyle("STAT_VALUE_NAME_" + StatFields[SingleField], "background-color:#ebffbc; font-weight:bold;font-size:22px;");
                        this.MainStatsForPrint += SingleField.replace("_", " ") + " : " + StatFields[SingleField] + ", ";
                    }
                    this.MainStatsForPrint += "<br>";
                    var StatFields = AllExtraItem;
                    for (var SingleField in StatFields) {
                        this.StatisticsGrid.addRow("STAT_KEY_NAME_" + SingleField, SingleField.replace("_", " "));
                        this.StatisticsGrid.addRow("STAT_VALUE_NAME_" + StatFields[SingleField], StatFields[SingleField]);
                        this.StatisticsGrid.setRowTextStyle("STAT_KEY_NAME_" + SingleField, "color:white;background-color: " + com.ordermanager.utilty.MainUtility.getRandomColorDark() + "; font-weight:bold;font-size:20px ");
                        this.StatisticsGrid.setRowTextStyle("STAT_VALUE_NAME_" + StatFields[SingleField], "background-color:#ebffbc; font-weight:bold;font-size:22px;");
                        this.MainStatsForPrint += SingleField.replace("_", " ") + " : " + StatFields[SingleField] + " ,";
                    }
                    progressOffCustom(this.ModifiedLayoutObject.cells("c"));
                };
                return PaymentPrintView;
            }());
            reportingutility.PaymentPrintView = PaymentPrintView;
        })(reportingutility = ordermanager.reportingutility || (ordermanager.reportingutility = {}));
    })(ordermanager = com.ordermanager || (com.ordermanager = {}));
})(com || (com = {}));
