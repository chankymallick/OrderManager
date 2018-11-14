declare var progressOffCustom: any;
declare var dhtmlXLayoutObject;
declare var Language: any;
declare var showFailedNotificationWithICON: any;
declare var $: any;
declare var dhtmlXList: any;
declare var getCurrentDate: any;
declare var dhx: any;
declare var enlargeCharts: any;

module com.ordermanager.utility {
    export class DataExplorer {
        public LayoutObject: any;
        constructor() {
            this.constructDataExplorerWindow();

        }
        public constructDataExplorerWindow() {
            var winObj = com.ordermanager.utilty.MainUtility.getModelWindow("Data Explorer", 1360, 620);
            this.LayoutObject = winObj.attachLayout({
                pattern: "2E",
                cells: [
                    { id: "a", text: "Options", height: 70, width: 775, header: false },
                    { id: "b", text: "Data",  header: false }
                    
                ]
            });
            
            var OrderListGrid = this.LayoutObject.cells("b").attachGrid();
            OrderListGrid.enableMultiline(true);
            OrderListGrid.enableAutoWidth(true);           
            OrderListGrid.setStyle(
                "", "font-weight:bold;", "color:red;", ""
            );
            OrderListGrid.load("/getDataExplorer?modulename=AUDIT&keyname=AUDIT_UID");
            OrderListGrid.attachEvent("onXLE", () => {

            });
            this.LayoutObject.cells("b").showInnerScroll();

        }

    }
}