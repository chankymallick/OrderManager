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
        })(reportingutility = ordermanager.reportingutility || (ordermanager.reportingutility = {}));
    })(ordermanager = com.ordermanager || (com.ordermanager = {}));
})(com || (com = {}));
