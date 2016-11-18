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
                        _this.ParameterForm.attachEvent("onButtonClick", function (name) {
                            _this.StatisticsCell.progressOn();
                            _this.StatisticsCell.attachURL("getStatistics?StatisticsName=" + _this.ReportName + "&ReportParams=" + _this.getReportParameters());
                            _this.StatisticsCell.progressOff();
                        });
                        _this.ParameterForm.setItemValue("ORDER_DATE=DATE", getCurrentDate());
                        _this.contructStatisticsLayout();
                    });
                };
                TransactionReports.prototype.contructStatisticsLayout = function () {
                    this.StatisticsCell = this.ModifiedLayoutObject.cells("a");
                    this.StatisticsCell.fixSize(true, true);
                    this.StatisticsCell.progressOn();
                    this.StatisticsCell.attachURL("getStatistics?StatisticsName=" + this.ReportName + "&ReportParams=" + this.getReportParameters());
                    this.StatisticsCell.progressOff();
                };
                TransactionReports.prototype.getReportParameters = function () {
                    if (this.ReportName === com.ordermanager.home.OrderManagerHome.REPORT_DAILY_ADVANCE) {
                        var params = this.ParameterForm.getValues();
                        var date = this.ParameterForm.getItemValue("ORDER_DATE=DATE", true);
                        params["ORDER_DATE=DATE"] = date;
                        params["LOCATION=STR"] = "BAGNAN";
                        return JSON.stringify(params);
                    }
                };
                TransactionReports.prototype.constructParameterForm = function () {
                    if (this.ParameterForm != null || this.ParameterForm != undefined) {
                        this.ParameterForm.unload();
                    }
                    this.ParameterForm = this.ModifiedLayoutObject.cells("b").attachForm();
                    this.ParameterForm.load(this.ReportName + "_Form");
                    this.setSpecificOnLoad();
                };
                return TransactionReports;
            }());
            reportingutility.TransactionReports = TransactionReports;
        })(reportingutility = ordermanager.reportingutility || (ordermanager.reportingutility = {}));
    })(ordermanager = com.ordermanager || (com.ordermanager = {}));
})(com || (com = {}));
