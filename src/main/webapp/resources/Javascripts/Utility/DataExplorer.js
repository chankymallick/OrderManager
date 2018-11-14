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
                    var winObj = com.ordermanager.utilty.MainUtility.getModelWindow("Data Explorer", 1360, 620);
                    this.LayoutObject = winObj.attachLayout({
                        pattern: "2E",
                        cells: [
                            { id: "a", text: "Options", height: 70, width: 775, header: false },
                            { id: "b", text: "Data", header: false }
                        ]
                    });
                    var OrderListGrid = this.LayoutObject.cells("b").attachGrid();
                    OrderListGrid.enableMultiline(true);
                    OrderListGrid.enableAutoWidth(true);
                    OrderListGrid.setStyle("", "font-weight:bold;", "color:red;", "");
                    OrderListGrid.load("/getDataExplorer?modulename=AUDIT&keyname=AUDIT_UID");
                    OrderListGrid.attachEvent("onXLE", function () {
                    });
                    this.LayoutObject.cells("b").showInnerScroll();
                };
                return DataExplorer;
            }());
            utility.DataExplorer = DataExplorer;
        })(utility = ordermanager.utility || (ordermanager.utility = {}));
    })(ordermanager = com.ordermanager || (com.ordermanager = {}));
})(com || (com = {}));
