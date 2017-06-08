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
                if (ReportType == "BY DATE") {
                    fromDate = this.QueryForm.getItemValue("FROM_DATE=DATE", true);
                    toDate = this.QueryForm.getItemValue("TO_DATE=DATE", true);
                }
                if (name == "getreport") {
                    this.counstructProductionGrid(ProductionType, Name, ReportType, fromDate, toDate);
                    this.statisticCounterConstruction(ProductionType, Name, ReportType, fromDate, toDate);
                }
                else {
                    this.counstructProductionGrid(ProductionType, Name, ReportType, fromDate, toDate);
                    this.statisticCounterConstruction(ProductionType, Name, ReportType, fromDate, toDate);
                    this.ReportGrid.attachEvent("onXLE", () => {
                        progressOffCustom(this.ModifiedLayoutObject.cells("b"));
                        this.ReportGrid.printView("<div> <div style='background-color:#e5e5e5;font-size:24px;display:table;font-weight:bold;'> MALLICK DRESSES </div> <div style='font-size:18px;float:left;font-weight:bold;'> ORDER ASSIGNMENT REPORT<br> NAME : " + Name + "<br> DATE : 31/01/2017 </div> </div>", "<div></div>");
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
            this.ReportGrid.enableColSpan(true);
            this.ReportGrid.enableMultiline(true);
            this.ReportGrid.load('getDayWiseProductionDetails2?ProductionType=' + ProductionType + '&Name=' + Name + '&ReportType=' + ReportType + "&fromDate=" + fromDate + "&toDate=" + toDate);
            this.ReportGrid.attachEvent("onXLE", () => {
                progressOffCustom(this.ModifiedLayoutObject.cells("b"));

            });
        }

        public statisticCounterConstruction(ProductionType: any, Name: any, ReportType: any, fromDate: any, toDate: any) {
            var statsData =  SynchronousGetAjaxRequest('getDayWiseProductionStats?ProductionType=' + ProductionType + '&Name=' + Name + '&ReportType=' + ReportType + "&fromDate=" + fromDate + "&toDate=" + toDate);
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

        }


    }
}
