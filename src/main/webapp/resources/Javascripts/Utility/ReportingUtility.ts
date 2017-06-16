/// <reference path="../Home/OrderManagerHome.ts"/>
/// <reference path="./Utility.ts"/>
declare var progressOffCustom: any;
declare var shortcut: any;
declare var showSuccessNotification: any;
declare var showFailedNotification: any;
declare var SynchronousGetAjaxRequest: any;
declare var showSuccessNotificationWithICON: any;
declare var showFailedNotificationWithICON: any;
declare var Language: any;
declare var dhtmlXWindows: any;
declare var getCurrentDate: any;
declare var $: any;
module com.ordermanager.reportingutility {
    export class TransactionReports {
        public ModifiedLayoutObject: any;
        public MainLayout: any;
        public ReportName: any;
        public NotificationCell: any;
        public StatisticsCell: any;
        public StatisticsDataViewObject: any;
        public ParameterForm: any;
        public ReportGrid: any;
        public ReportGridParams: any;
        constructor(layoutCell: any, notificationCell: any, reportName: any) {
            this.MainLayout = layoutCell;
            this.ReportName = reportName;
            this.NotificationCell = notificationCell;
            this.constructInnerLayoutForReport();
        }
        public constructInnerLayoutForReport() {
            this.ModifiedLayoutObject = this.MainLayout.attachLayout({
                pattern: "3U",
                cells: [
                    {id: "a", text: "Statistics", height: 170, width: 775, header: false},
                    {id: "b", text: "Inputs", height: 170, header: false},
                    {id: "c", text: "Details", header: false}
                ]
            });
            //this.contructStatisticsLayout();
            this.constructParameterForm();

        }
        public setSpecificOnLoad() {
            this.ParameterForm.attachEvent("onXLE", () => {
                this.ParameterForm.setItemValue("ORDER_DATE=DATE", getCurrentDate());
                this.ReportGridParams = this.getReportParameters();
                this.constructReportGrid();
                this.ParameterForm.attachEvent("onButtonClick", (name) => {
                    this.ReportGridParams = this.getReportParameters();
                    this.ReportGrid.load("LoadReportViewGrid?gridname=" + this.ReportName + "&ParamJson=" + this.ReportGridParams);
                    this.StatisticsCell.progressOn();
                    this.StatisticsCell.attachURL("getStatistics?StatisticsName=" + this.ReportName + "&ReportParams=" + this.ReportGridParams);
                    this.StatisticsCell.progressOff();
                });
                this.contructStatisticsLayout();
            });
        }
        public contructStatisticsLayout() {
            this.StatisticsCell = this.ModifiedLayoutObject.cells("a");
            this.StatisticsCell.fixSize(true, true);
            this.ModifiedLayoutObject.cells("a").progressOn();
            this.StatisticsCell.attachURL("getStatistics?StatisticsName=" + this.ReportName + "&ReportParams=" + this.getReportParameters());
            progressOffCustom(this.ModifiedLayoutObject.cells("a"));
        }
        public getReportParameters() {
            var params = this.ParameterForm.getValues();
            if (this.ReportName === com.ordermanager.home.OrderManagerHome.REPORT_DAILY_ADVANCE) {
                var date = this.ParameterForm.getItemValue("ORDER_DATE=DATE", true);
                params["ORDER_DATE=DATE"] = date;
                params["LOCATION=STR"] = "BAGNAN";
            }
            return JSON.stringify(params);

        }
        public constructParameterForm() {
            if (this.ParameterForm != null || this.ParameterForm != undefined) {
                this.ParameterForm.unload();
            }
            this.ParameterForm = this.ModifiedLayoutObject.cells("b").attachForm();
            this.ParameterForm.load(this.ReportName + "_Form");
            this.setSpecificOnLoad();
        }
        public constructReportGrid() {
            this.ModifiedLayoutObject.cells("c").progressOn();
            this.ReportGrid = this.ModifiedLayoutObject.cells("c").attachGrid();
            this.ReportGrid.load("LoadReportViewGrid?gridname=" + this.ReportName + "&ParamJson=" + this.ReportGridParams);
            this.ReportGrid.attachEvent("onXLE", () => {
                progressOffCustom(this.ModifiedLayoutObject.cells("c"));
            });

        }
    }
    export class DayWiseProductionReport {
        public ModifiedLayoutObject: any;
        public MainLayout: any;
        public ReportName: any;
        public NotificationCell: any;
        public StatisticsCell: any;
        public StatisticsGrid: any;
        public QueryForm: any;
        public ReportGrid: any;
        public ReportGridParams: any;
        public MainStatsForPrint: any;
        public ExtraStatsForPrint: any;

        constructor(layoutCell: any, notificationCell: any) {
            this.MainLayout = layoutCell;
            this.NotificationCell = notificationCell;
            this.initProductionReportLayout();
        }

        public initProductionReportLayout() {
            this.ModifiedLayoutObject = this.MainLayout.attachLayout({
                pattern: "3T",
                cells: [
                    {id: "a", text: "Query", header: false, height: 100},
                    {id: "b", text: "All Production", header: true, width: 1000},
                    {id: "c", text: "Statistics", header: true}
                ]
            });
            this.constructQueryForm();
        }
        public constructQueryForm() {
            this.ModifiedLayoutObject.progressOn();
            if (this.QueryForm != null || this.QueryForm != undefined) {
                this.QueryForm.unload();
            }
            this.QueryForm = this.ModifiedLayoutObject.cells("a").attachForm();
            this.QueryForm.load("daily_productionReport_Form");
            this.QueryForm.attachEvent("onEnter", () => {
                // var ParameterValue = this.QueryForm.getValues();
                // this.loadAssignmentGridData(ParameterValue);
            });
            this.QueryForm.attachEvent("onXLE", () => {
                this.QueryForm.setFocusOnFirstActive();
                // this.setSpecificOnLoad();
                progressOffCustom(this.ModifiedLayoutObject);
            });
            this.QueryForm.attachEvent("onChange", (name, value) => {
                if (name == "TYPE=STR") {
                    this.ModifiedLayoutObject.progressOn();
                    com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(this.QueryForm.getOptions("NAME=STR"), "EMPLOYEES", "EMP_NAME", "EMP_ROLE", value);
                    progressOffCustom(this.ModifiedLayoutObject);
                }
                if (name == "REPORT_TYPE=STR") {
                    if (value == "BY DATE") {
                        this.QueryForm.enableItem("FROM_DATE=DATE");
                        this.QueryForm.enableItem("TO_DATE=DATE");
                    }
                    else {
                        this.QueryForm.disableItem("FROM_DATE=DATE");
                        this.QueryForm.disableItem("TO_DATE=DATE");
                    }
                }

            });
            this.QueryForm.attachEvent("onButtonClick", (name) => {
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
                else {
                    this.counstructProductionGrid(ProductionType, Name, ReportType, fromDate, toDate);
                    this.statisticCounterConstruction(ProductionType, Name, ReportType, fromDate, toDate);
                    this.ReportGrid.attachEvent("onXLE", () => {
                        progressOffCustom(this.ModifiedLayoutObject.cells("b"));

                        this.ReportGrid.printView("<div> <div style='background-color:#e5e5e5;font-size:24px;display:table;font-weight:bold;'> MALLICK DRESSES </div> <div style='font-size:18px;float:left;font-weight:bold;'> ORDER ASSIGNMENT REPORT <br>" + this.MainStatsForPrint + " </div> </div>", "<div></div>");
                    });
                }
            });

        }
        public getReportParameters() {
            var params = this.QueryForm.getValues();
            if (this.ReportName === com.ordermanager.home.OrderManagerHome.REPORT_DAILY_ADVANCE) {
                var date = this.QueryForm.getItemValue("ORDER_DATE=DATE", true);
                params["ORDER_DATE=DATE"] = date;
                params["LOCATION=STR"] = "BAGNAN";
            }
            return JSON.stringify(params);

        }
        public counstructProductionGrid(ProductionType: any, Name: any, ReportType: any, fromDate: any, toDate: any) {
            this.ModifiedLayoutObject.cells("b").progressOn();
            this.ReportGrid = this.ModifiedLayoutObject.cells("b").attachGrid();
            this.ReportGrid.setStyle("background-color:#003eba;color:white; font-weight:bold;", "font-weight:bold;", "", "");
            this.ReportGrid.enableColSpan(true);
            this.ReportGrid.enableMultiline(true);
            this.ReportGrid.load('getDayWiseProductionDetails2?ProductionType=' + ProductionType + '&Name=' + Name + '&ReportType=' + ReportType + "&fromDate=" + fromDate + "&toDate=" + toDate);
            this.ReportGrid.attachEvent("onXLE", () => {
                progressOffCustom(this.ModifiedLayoutObject.cells("b"));

            });
        }

        public statisticCounterConstruction(ProductionType: any, Name: any, ReportType: any, fromDate: any, toDate: any) {
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
                this.MainStatsForPrint += SingleField.replace("_", " ") + " : " + StatFields[SingleField] + ", "
            }
            this.MainStatsForPrint += "<br>";
            var StatFields = AllExtraItem;
            for (var SingleField in StatFields) {
                this.StatisticsGrid.addRow("STAT_KEY_NAME_" + SingleField, SingleField.replace("_", " "));
                this.StatisticsGrid.addRow("STAT_VALUE_NAME_" + StatFields[SingleField], StatFields[SingleField]);
                this.StatisticsGrid.setRowTextStyle("STAT_KEY_NAME_" + SingleField, "color:white;background-color: " + com.ordermanager.utilty.MainUtility.getRandomColorDark() + "; font-weight:bold;font-size:20px ");
                this.StatisticsGrid.setRowTextStyle("STAT_VALUE_NAME_" + StatFields[SingleField], "background-color:#ebffbc; font-weight:bold;font-size:22px;");
                this.MainStatsForPrint += SingleField.replace("_", " ") + " : " + StatFields[SingleField] + " ,"
            }
            progressOffCustom(this.ModifiedLayoutObject.cells("c"))
        }


    }

    export class WagePaymentSystem {
        public ModifiedLayoutObject: any;
        public MainLayout: any;
        public ReportName: any;
        public NotificationCell: any;
        public StatisticsCell: any;
        public StatisticsGrid: any;
        public QueryForm: any;
        public ReportGrid: any;
        public ReportGridParams: any;
        public MainStatsForPrint: any;
        public ExtraStatsForPrint: any;
        public DataTabber: any;
        public DeletedOrderGrid: any;
        public removedBills = [];
        public OperationToolbar: any;
        public ParameterJSON = {};

        constructor(layoutCell: any, notificationCell: any) {
            this.MainLayout = layoutCell;
            this.NotificationCell = notificationCell;
            this.initProductionReportLayout();
        }

        public initProductionReportLayout() {

            this.ModifiedLayoutObject = this.MainLayout.attachLayout({
                pattern: "3T",
                cells: [
                    {id: "a", text: "Query", header: false, height: 100},
                    {id: "b", text: "All Production", header: true, width: 1000},
                    {id: "c", text: "Statistics", header: true}
                ]
            });
            this.constructQueryForm();
            this.constructToolBar();
        }
        public constructToolBar() {
            this.OperationToolbar = this.ModifiedLayoutObject.attachToolbar();
            this.OperationToolbar.loadStruct("wagePaymentUnpaidToolbar?formname=wagePayment", "json");
            this.OperationToolbar.attachEvent("onClick", (id) => {
                if (id === "pay") {
                    this.initiateWagePaymentProcess();
                }
                if (id === "print") {
                    new com.ordermanager.reportingutility.PaymentPrintView("MANUAL","","","");
                }
            });
        }
        public constructQueryForm() {
            this.ModifiedLayoutObject.progressOn();
            if (this.QueryForm != null || this.QueryForm != undefined) {
                this.QueryForm.unload();
            }
            this.QueryForm = this.ModifiedLayoutObject.cells("a").attachForm();
            this.QueryForm.load("daily_productionReport_Form");
            this.QueryForm.attachEvent("onEnter", () => {
                // var ParameterValue = this.QueryForm.getValues();
                // this.loadAssignmentGridData(ParameterValue);
            });
            this.QueryForm.attachEvent("onXLE", () => {
                this.QueryForm.setFocusOnFirstActive();
                // this.setSpecificOnLoad();
                progressOffCustom(this.ModifiedLayoutObject);
            });
            this.QueryForm.attachEvent("onChange", (name, value) => {
                if (name == "TYPE=STR") {
                    this.ModifiedLayoutObject.progressOn();
                    com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(this.QueryForm.getOptions("NAME=STR"), "EMPLOYEES", "EMP_NAME", "EMP_ROLE", value);
                    progressOffCustom(this.ModifiedLayoutObject);
                }
                if (name == "REPORT_TYPE=STR") {
                    if (value == "BY DATE") {
                        this.QueryForm.enableItem("FROM_DATE=DATE");
                        this.QueryForm.enableItem("TO_DATE=DATE");
                    }
                    else {
                        this.QueryForm.disableItem("FROM_DATE=DATE");
                        this.QueryForm.disableItem("TO_DATE=DATE");
                    }
                }

            });
            this.QueryForm.attachEvent("onButtonClick", (name) => {
                this.reportEventActions(name);
            });

        }
        public reportEventActions(name) {
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
                this.ReportGrid.attachEvent("onXLE", () => {
                    progressOffCustom(this.ModifiedLayoutObject.cells("b"));

                    this.ReportGrid.printView("<div> <div style='background-color:#e5e5e5;font-size:24px;display:table;font-weight:bold;'> MALLICK DRESSES </div> <div style='font-size:18px;float:left;font-weight:bold;'> ORDER ASSIGNMENT REPORT <br>" + this.MainStatsForPrint + " </div> </div>", "<div></div>");
                });
            }
        }
        public getReportParameters() {
            var params = this.QueryForm.getValues();
            if (this.ReportName === com.ordermanager.home.OrderManagerHome.REPORT_DAILY_ADVANCE) {
                var date = this.QueryForm.getItemValue("ORDER_DATE=DATE", true);
                params["ORDER_DATE=DATE"] = date;
                params["LOCATION=STR"] = "BAGNAN";
            }
            return JSON.stringify(params);

        }
        public counstructProductionGrid(ProductionType: any, Name: any, ReportType: any, fromDate: any, toDate: any) {
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
            this.ReportGrid.attachEvent("onXLE", () => {
                progressOffCustom(this.ModifiedLayoutObject.cells("b"));
            });
            this.DeletedOrderGrid = this.DataTabber.tabs("DeletedItems").attachGrid();
            this.DeletedOrderGrid.setStyle("background-color:#003eba;color:white; font-weight:bold;", "font-weight:bold;", "", "");
            this.DeletedOrderGrid.setHeader("NO,BILL,MAIN_STATUS,LOCATION,WAGE,WAGE_STATUS,ITEMS,QTY,ORDER_TYPE,VENDOR,REMOVE");
            this.DeletedOrderGrid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
            this.DeletedOrderGrid.setColAlign("center,center,center,center,center,center,center,center,center,center,center");
            this.DeletedOrderGrid.init();

            this.ReportGrid.attachEvent("onRowSelect", (id, index) => {
                var BillNo = this.ReportGrid.cells(id, 1).getValue();
                dhtmlx.confirm({
                    type: "confirm",
                    text: "Do you want to remove Bill Number " + BillNo + " ?",
                    callback: (result) => {
                        if (result) {
                            var SourceRowId = id;
                            var TargetRowId = this.DeletedOrderGrid.getRowId(this.DeletedOrderGrid.getRowsNum());
                            this.moveOrderGrid(SourceRowId, "", this.ReportGrid, this.DeletedOrderGrid);
                            showSuccessNotification(BillNo + " removed from List");
                        }
                    }
                });

            });
            this.DeletedOrderGrid.attachEvent("onRowAdded", (rId) => {
                this.removedBills = [];
                var index = 1;
                this.DeletedOrderGrid.forEachRow((id) => {
                    this.DeletedOrderGrid.cells(id, 0).setValue(index);
                    this.removedBills.push(this.DeletedOrderGrid.cells(id, 1).getValue().toString());
                    index += 1;
                });
                this.DataTabber.tabs("DeletedItems").setText("Deleted Orders (" + (index - 1) + ")");

            });

        }
        public getRemovedBills() {
            return "'" + this.removedBills.join("','") + "'";
        }
        public moveOrderGrid(SourceRowID: any, TargetRowID: any, SourceGridObject: any, TargetGridObject: any) {
            this.ModifiedLayoutObject.progressOn();
            this.ReportGrid.moveRowTo(SourceRowID, null, "move", "sibling", SourceGridObject, TargetGridObject);
            this.reportEventActions("refreshCounter");
            progressOffCustom(this.ModifiedLayoutObject);
        }
        public statisticCounterConstruction(ProductionType: any, Name: any, ReportType: any, fromDate: any, toDate: any) {
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
                this.MainStatsForPrint += SingleField.replace("_", " ") + " : " + StatFields[SingleField] + ", "
            }
            this.MainStatsForPrint += "<br>";
            var StatFields = AllExtraItem;
            for (var SingleField in StatFields) {
                this.StatisticsGrid.addRow("STAT_KEY_NAME_" + SingleField, SingleField.replace("_", " "));
                this.StatisticsGrid.addRow("STAT_VALUE_NAME_" + StatFields[SingleField], StatFields[SingleField]);
                this.StatisticsGrid.setRowTextStyle("STAT_KEY_NAME_" + SingleField, "color:white;background-color: " + com.ordermanager.utilty.MainUtility.getRandomColorDark() + "; font-weight:bold;font-size:20px ");
                this.StatisticsGrid.setRowTextStyle("STAT_VALUE_NAME_" + StatFields[SingleField], "background-color:#ebffbc; font-weight:bold;font-size:22px;");
                this.MainStatsForPrint += SingleField.replace("_", " ") + " : " + StatFields[SingleField] + " ,"
            }
            progressOffCustom(this.ModifiedLayoutObject.cells("c"))
        }

        public initiateWagePaymentProcess() {
            this.ModifiedLayoutObject.progressOn();
            var ProductionType = "TO_" + this.QueryForm.getItemValue("TYPE=STR", true);
            var Name = this.QueryForm.getItemValue("NAME=STR", true);
            var AllBillNos = [];
            this.ReportGrid.forEachRow((id) => {
                var Data = this.ReportGrid.cells(id, 1).getValue();
                if (Data.indexOf("/") < 0) {
                    AllBillNos.push(Data);
                }
            });
            this.ParameterJSON["ALL_BILL_NO"] = AllBillNos;
            var Response = SynchronousGetAjaxRequest("wagePayment?ProductionType=" + ProductionType + "&Name=" + Name + "&BILLS=" + JSON.stringify(this.ParameterJSON))
            if (Response.RESPONSE_STATUS === "SUCCESS") {
                this.ReportGrid.forEachRow((id) => {
                    var BILLNO = this.ReportGrid.cells(id, 1).getValue()
                    if (BILLNO.indexOf("/") < 0) {
                        this.ReportGrid.setUserData(id, "SERVER_DATA", Response.RESPONSE_VALUE[BILLNO]);
                        if (Response.RESPONSE_VALUE[BILLNO].split(",")[0].indexOf("SUCCES") == 0) {
                            this.ReportGrid.cells(id, 10).setValue("");
                            this.ReportGrid.setRowTextStyle(id, "color:#0026ff;background-color: #cccccc; font-weight:bold; ");
                            this.ReportGrid.cells(id, 10).setValue("<img height='23px' width='20px' src='resources/Images/success.png'/>");
                        }
                        else {
                            this.ReportGrid.cells(id, 10).setValue("<a><img height='20px' width='20px' src='resources/Images/failed.png'/></a>");
                        }
                    }
                });
                progressOffCustom(this.ModifiedLayoutObject);
                showSuccessNotificationWithICON(Response.RESPONSE_MESSAGE);
                this.NotificationCell.attachHTMLString(" <b style='color:red'>" + Response.RESPONSE_MESSAGE + "</b><br>" + JSON.stringify(Response.RESPONSE_VALUE));
                this.NotificationCell.expand();
                new com.ordermanager.reportingutility.PaymentPrintView("DIRECT",ProductionType,Name,getCurrentDate());
            }
            else {

                showFailedNotificationWithICON(Response.RESPONSE_MESSAGE);
                this.NotificationCell.attachHTMLString(" <b style='color:red'>" + Response.RESPONSE_VALUE.EXCEPTION_MESSAGE + "</b>");
                this.NotificationCell.expand();

            }
            progressOffCustom(this.ModifiedLayoutObject);
        }
    }

    export class PaymentPrintView {

        public MainWindow: any;
        public ModifiedLayoutObject: any;
        public OperationToolbar: any;
        public QueryForm;
        public ReportGrid: any;
        public MainStatsForPrint: any;
        public StatisticsGrid: any;
        public TOTAL_WAGE:any;
        constructor(PrintType: any,ProductionType:any,Name:any,paymentDate:any) {
            var height = $(window).height();
            this.MainWindow = com.ordermanager.utilty.MainUtility.getModelWindow("Payment Printing Module", 1200, height - 50);
            this.MainWindow.show();

            this.ModifiedLayoutObject = this.MainWindow.attachLayout({
                pattern: "3T",
                cells: [
                    {id: "a", text: "Query", header: false, width: 900, height: 80},
                    {id: "b", text: "All Production", header: false, width: 900},
                    {id: "c", text: "Stats", header: false},
                ]
            });
            this.OperationToolbar = this.ModifiedLayoutObject.attachToolbar();
            this.OperationToolbar.loadStruct("printModuleToolbar?formname=printModule", "json");
            this.OperationToolbar.attachEvent("onClick", (id) => {
                if (id === "pay") {

                }
                if (id === "print") {

                }
            });
            this.constructQueryForm();
            if (PrintType == "DIRECT"){  
                this.MainStatsForPrint = "NAME : " + Name + "<br>";           
                this.counstructProductionGrid(ProductionType, Name, paymentDate);
                this.statisticCounterConstruction(ProductionType, Name, paymentDate);               
                this.ReportGrid.attachEvent("onXLE", () => {
                    this.ReportGrid.printView("<div> <div style='background-color:#e5e5e5;font-size:24px;display:table;font-weight:bold;'> MALLICK DRESSES </div> <div style='font-size:18px;float:left;font-weight:bold;'> WAGE PAYMENT RECEIPT <br> <div style='font-size:24px;display:table;font-weight:bold;'>TOTAL PAYMENT : " + this.TOTAL_WAGE+"/-</div> " + this.MainStatsForPrint + " </div> </div>", "<div></div>");
                });            
            }
        }
        public constructQueryForm() {
            this.ModifiedLayoutObject.progressOn();
            if (this.QueryForm != null || this.QueryForm != undefined) {
                this.QueryForm.unload();
            }
            this.QueryForm = this.ModifiedLayoutObject.cells("a").attachForm();
            this.QueryForm.load("print_module_form");
            this.QueryForm.attachEvent("onEnter", () => {
            });
            this.QueryForm.attachEvent("onXLE", () => {
                this.QueryForm.setFocusOnFirstActive();
                progressOffCustom(this.ModifiedLayoutObject);
            });
            this.QueryForm.attachEvent("onChange", (name, value) => {
                if (name == "TYPE=STR") {
                    this.ModifiedLayoutObject.progressOn();
                    com.ordermanager.utilty.MainUtility.setDynamicSelectBoxOptions(this.QueryForm.getOptions("NAME=STR"), "EMPLOYEES", "EMP_NAME", "EMP_ROLE", value);
                    progressOffCustom(this.ModifiedLayoutObject);
                }
            });
            this.QueryForm.attachEvent("onButtonClick", (name) => {
                var ProductionType = "TO_" + this.QueryForm.getItemValue("TYPE=STR", true);
                var Name = this.QueryForm.getItemValue("NAME=STR", true);
                var paymentDate = this.QueryForm.getItemValue("PAY_DATE=DATE", true);
                this.MainStatsForPrint = "NAME : " + Name + "<br>";           
                this.counstructProductionGrid(ProductionType, Name, paymentDate);
                this.statisticCounterConstruction(ProductionType, Name, paymentDate);               
                this.ReportGrid.attachEvent("onXLE", () => {
                    this.ReportGrid.printView("<div> <div style='background-color:#e5e5e5;font-size:24px;display:table;font-weight:bold;'> MALLICK DRESSES </div> <div style='font-size:18px;float:left;font-weight:bold;'> WAGE PAYMENT RECEIPT <br> <div style='font-size:24px;display:table;font-weight:bold;'>TOTAL PAYMENT : " + this.TOTAL_WAGE+"/-</div> " + this.MainStatsForPrint + " </div> </div>", "<div></div>");
                });
            });

        }
        public counstructProductionGrid(ProductionType: any, Name: any, PaymentDate: any) {
            this.ModifiedLayoutObject.progressOn();
            this.ReportGrid = this.ModifiedLayoutObject.cells("b").attachGrid();
            this.ReportGrid.setStyle("background-color:#003eba;color:white; font-weight:bold;", "font-weight:bold;", "", "");
            this.ReportGrid.enableColSpan(true);
            this.ReportGrid.enableMultiline(true);
            this.ReportGrid.load('getDayWisePaidWagePaymentOrders?ProductionType=' + ProductionType + '&Name=' + Name + '&PaymentDate=' + PaymentDate);
            this.ReportGrid.attachEvent("onXLE", () => {              
                progressOffCustom(this.ModifiedLayoutObject);
            });
        }
        public statisticCounterConstruction(ProductionType: any, Name: any, PaymentDate: any) {
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
                this.MainStatsForPrint += SingleField.replace("_", " ") + " : " + StatFields[SingleField] + ", "
            }
            this.MainStatsForPrint += "<br>";
            var StatFields = AllExtraItem;
            for (var SingleField in StatFields) {
                this.StatisticsGrid.addRow("STAT_KEY_NAME_" + SingleField, SingleField.replace("_", " "));
                this.StatisticsGrid.addRow("STAT_VALUE_NAME_" + StatFields[SingleField], StatFields[SingleField]);
                this.StatisticsGrid.setRowTextStyle("STAT_KEY_NAME_" + SingleField, "color:white;background-color: " + com.ordermanager.utilty.MainUtility.getRandomColorDark() + "; font-weight:bold;font-size:20px ");
                this.StatisticsGrid.setRowTextStyle("STAT_VALUE_NAME_" + StatFields[SingleField], "background-color:#ebffbc; font-weight:bold;font-size:22px;");
                this.MainStatsForPrint += SingleField.replace("_", " ") + " : " + StatFields[SingleField] + " ,"
            }
            progressOffCustom(this.ModifiedLayoutObject.cells("c"))
        }
    }


}
