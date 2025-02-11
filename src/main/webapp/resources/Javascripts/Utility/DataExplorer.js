var com;
(function (com) {
    var ordermanager;
    (function (ordermanager) {
        var utility;
        (function (utility) {
            var DataExplorer = /** @class */ (function () {
                function DataExplorer() {
                    this.constructDataExplorerWindow();
                }
                DataExplorer.prototype.constructDataExplorerWindow = function () {
                    var _this = this;
                    var winObj = com.ordermanager.utilty.MainUtility.getModelWindow("Data Explorer", 1360, 620);
                    this.LayoutObject = winObj.attachLayout({
                        pattern: "2E",
                        cells: [
                            { id: "a", text: "Options", height: 40, header: false },
                            { id: "b", text: "Data", header: false }
                        ]
                    });
                    this.QueryForm = this.LayoutObject.cells("a").attachForm();
                    this.QueryForm.load("/getDataExplorerForm");
                    var OrderListGrid = this.LayoutObject.cells("b").attachGrid();
                    OrderListGrid.enableMultiline(true);
                    OrderListGrid.enableAutoWidth(true);
                    OrderListGrid.setStyle("", "font-weight:bold;", "color:red;", "");
                    OrderListGrid.attachEvent("onXLE", function () {
                    });
                    this.QueryForm.attachEvent("onButtonClick", function (name) {
                        var module = _this.QueryForm.getItemValue("module=str");
                        OrderListGrid.load("/getDataExplorer?modulename=" + module.split('-')[0] + "&keyname=" + module.split('-')[1]);
                    });
                    this.LayoutObject.cells("b").showInnerScroll();
                };
                return DataExplorer;
            }());
            utility.DataExplorer = DataExplorer;
        })(utility = ordermanager.utility || (ordermanager.utility = {}));
    })(ordermanager = com.ordermanager || (com.ordermanager = {}));
})(com || (com = {}));
