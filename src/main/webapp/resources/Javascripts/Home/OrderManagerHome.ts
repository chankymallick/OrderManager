/// <reference path="..//Utility/Utility.ts"/>
/// <reference path="..//Utility/UpdateUtility.ts"/>
/// <reference path="../Utility/BulkUpdate.ts"/>
declare var progressOffCustom: any;
declare var dhtmlXLayoutObject;
declare var Language: any;
declare var showFailedNotificationWithICON: any;
module com.ordermanager.home {
    export class CommandHandler {

        public static CODE_FORM_NEW_ITEM = "ANI";
        public static CODE_FORM_NEW_USER = "ANU";
        public static CODE_FORM_NEW_ORDER = "ANO";
        public static CODE_ADD_NEW_STATUS_TYPE= "ANST";
        public static CODE_QUICK_NEW_ORDER = "AQA"
        public static CODE_QUICK_NEW_LOCATION = "ANL";
        public static CODE_ADD_NEW_EMPLOYEE= "ANE";
        //----------------------------------------
        public static CODE_REPORT_DAILY_ADVANCE = "RDA";
        //----------------------------------------
        public static CODE_UPDATE_NEW_ORDER = "UNO";
        public static CODE_UPDATE_ADVANCE = "UAD";
        public static CODE_BULK_UPDATE_MASTER_TAILOR_ASSIGNMENT = "UBMT";
    }
    export class OrderManagerHome {
        public static FORM_NEW_ITEM = "addNewItem";
        public static FORM_NEW_USER = "addNewUser";
        public static FORM_NEW_ORDER = "addNewOrder";
        public static FORM_QUICK_NEW_ORDER = "quickNewOrder";
        public static FORM_ADD_NEW_STATUS_TYPE ="addNewStatusType";
        public static FORM_ADD_NEW_LOCATION = "addNewLocation";
        public static FORM_ADD_NEW_EMPLOYEE = "addNewEmployee";
        //---------------------------------------------------
        public static REPORT_DAILY_ADVANCE = "advanceReport";
        //---------------------------------------------------
        public static UPDATE_NEW_ORDER ="updateNewOrder";
        public static UPDATE_ADVANCE = "addadvance";
        public static UPDATE_BULK_MASTER_TAILOR = "updateBulkMasterTailor";


        public HomeLayoutObject: any;
        public HomeToolbar: any;
        public MenuAccordionObj: any;
        public MenuGrid: any;
        public FormEntryManagerObject: any;
        public ReportViewManagerObject: any;
        public UpdateManagerObject:any;
        constructor() {
            this.initLayout();
            this.commandRegister();
            this.HomeLayoutObject.attachEvent("onCollapse", (name) => {
                if (name === "a") {
                    this.commandRegister();
                }
            });
            this.HomeLayoutObject.attachEvent("onExpand", (name) => {
                if (name === "a") {
                    this.commandRegister();
                }
            });
        }
        public commandRegister() {
            shortcut.add("Home", () => {
                (<HTMLInputElement>document.getElementById("searchCode")).value = "";
                document.getElementById("searchCode").focus();
            }, {
                    'type': 'keyup',
                    'disable_in_input': false,
                    'target': document,
                    'propagate': true
                });
            shortcut.add("Enter", () => {
                var command = (<HTMLInputElement>document.getElementById("searchCode")).value
                if (command.trim().toUpperCase() === CommandHandler.CODE_FORM_NEW_ITEM) {
                    this.menuActionIntializer(OrderManagerHome.FORM_NEW_ITEM, 200);
                }
                if (command.trim().toUpperCase() === CommandHandler.CODE_ADD_NEW_EMPLOYEE) {
                    this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_EMPLOYEE, 200);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_FORM_NEW_USER) {
                    this.menuActionIntializer(OrderManagerHome.FORM_NEW_USER, 200);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_FORM_NEW_ORDER) {
                    this.menuActionIntializer(OrderManagerHome.FORM_NEW_ORDER, 170);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_QUICK_NEW_ORDER) {
                    this.menuActionIntializer(OrderManagerHome.FORM_QUICK_NEW_ORDER, 220);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_ADD_NEW_STATUS_TYPE) {
                    this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_STATUS_TYPE, 220);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_QUICK_NEW_LOCATION) {
                    this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_LOCATION, 220);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_REPORT_DAILY_ADVANCE) {
                    this.menurReportsActionIntializer(OrderManagerHome.REPORT_DAILY_ADVANCE);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_UPDATE_NEW_ORDER) {
                  this.menuUpdateActionInitializer(OrderManagerHome.UPDATE_NEW_ORDER,100);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_UPDATE_ADVANCE) {
                  this.menuUpdateActionInitializer(OrderManagerHome.UPDATE_ADVANCE,100);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_BULK_UPDATE_MASTER_TAILOR_ASSIGNMENT) {
                  this.menuBulkUpdateActionInitializer(OrderManagerHome.UPDATE_BULK_MASTER_TAILOR,100);
                }
                else {
                    showFailedNotificationWithICON(command.trim().toUpperCase() + ": Command Not Found");
                }
            }, {
                    'type': 'keyup',
                    'disable_in_input': false,
                    'target': document.getElementById("searchCode"),
                    'propagate': true
                });
        }
        public initLayout() {
            this.HomeLayoutObject = new dhtmlXLayoutObject({
                parent: "Layout_Container",
                pattern: "3L",
                cells:
                [
                    { id: "a", text: "Menu", width: 250 },
                    { id: "b", text: "Dashboard" },
                    { id: "c", text: "Notifications", height: 150, collapse: true }
                ]
            });
            this.HomeLayoutObject.progressOn();
            this.HomeLayoutObject.cells("a").setText(Language.menu + "<span>&nbsp;&nbsp;<input type='text' id='searchCode' placeholder='Shortcut Command'/></span>");
            this.HomeToolbar = this.HomeLayoutObject.attachToolbar();
            this.HomeToolbar.addText("appname", 1, "<span style='font-weight:bold'>Mallick Dresses Order Manager 1.0</span>");
            this.MenuAccordionObj = this.HomeLayoutObject.cells("a").attachAccordion();
            this.HomeLayoutObject.cells("a").showHeader();
            this.MenuAccordionObj.addItem("ordersandbills", Language.orderandbill);
            this.MenuAccordionObj.addItem("mastertailor", Language.masterandtailor);
            this.MenuAccordionObj.addItem("labourmanager", Language.labourmanager);
            this.MenuAccordionObj.addItem("salesmanmanager", Language.salesmanmanager);
            this.MenuAccordionObj.addItem("usermanager", Language.usermanagerment);
            this.MenuAccordionObj.addItem("supplierbook", Language.suppliersbook);
            this.MenuGrid = this.MenuAccordionObj.cells("ordersandbills").attachGrid();
            this.loadMenuItems("ordersandbills");
            this.MenuAccordionObj.attachEvent("onActive", (id, state) => {
                this.loadMenuItems(id)
            });
        }
        public skinChanger(dhtmlxObject: any, skinType: any) {
            if (skinType === "white") {
                dhtmlxObject.setSkin("dhx_web");
            }
        }
        public menuActionIntializer(actionName, dataviewcellheight) {
            this.HomeLayoutObject.cells("a").expand();
            this.FormEntryManagerObject = new com.ordermanager.utilty.FormEntryManager(this.HomeLayoutObject.cells("b"), this.HomeLayoutObject.cells("c"), actionName, 0, dataviewcellheight);
            this.HomeLayoutObject.cells("c").showHeader();
        }
        public menuUpdateActionInitializer(actionName,QueryFormHeight){
          this.HomeLayoutObject.cells("a").expand();
          this.UpdateManagerObject = new com.ordermanager.UpdateUtility.UpdateUtility(actionName,this.HomeLayoutObject.cells("b"), this.HomeLayoutObject.cells("c"), QueryFormHeight);
          this.HomeLayoutObject.cells("c").showHeader();

        }
        public menuBulkUpdateActionInitializer(actionName,QueryFormHeight){
          this.HomeLayoutObject.cells("a").collapse();
          this.UpdateManagerObject = new com.ordermanager.bulkupdate.BulkUpdate(actionName,this.HomeLayoutObject.cells("b"), this.HomeLayoutObject.cells("c"), QueryFormHeight,5);
          this.HomeLayoutObject.cells("c").showHeader();

        }
        public menurReportsActionIntializer(actionName) {
            this.HomeLayoutObject.cells("a").collapse();
            this.ReportViewManagerObject = new com.ordermanager.reportingutility.TransactionReports(this.HomeLayoutObject.cells("b"), this.HomeLayoutObject.cells("c"), actionName);
            this.HomeLayoutObject.cells("c").showHeader();


        }
        public loadMenuItems(itemtype: any) {
            if (this.MenuGrid != null || this.MenuGrid === undefined) {
                this.MenuGrid.destructor();
            }
            this.MenuGrid = this.MenuAccordionObj.cells(itemtype).attachGrid();
            this.MenuGrid.setHeader("image,name");
            this.MenuGrid.setNoHeader(true);
            this.MenuGrid.setInitWidths("50,180");
            this.MenuGrid.setColAlign("left,left");
            this.MenuGrid.setColTypes("ro,ro");
            this.MenuGrid.enableRowsHover(true, "GridHover");
            this.MenuGrid.init();
            this.MenuGrid.attachEvent("onXLE", () => {
                progressOffCustom(this.HomeLayoutObject);
            });
            this.MenuGrid.load("LoadMenuItems?menutype=" + itemtype);
            this.MenuGrid.attachEvent("onRowSelect", (id, ind) => {
                if (id === "AddNewItem") {
                    this.menuActionIntializer(OrderManagerHome.FORM_NEW_ITEM, 200);
                }
                if (id === "addnewuser") {
                    this.menuActionIntializer(OrderManagerHome.FORM_NEW_USER, 200);
                }
                if (id === "addneworder") {
                    this.menuActionIntializer(OrderManagerHome.FORM_NEW_ORDER, 170);
                }
                if (id === "quickNewOrder") {
                    this.menuActionIntializer(OrderManagerHome.FORM_QUICK_NEW_ORDER, 220);
                }
                if (id === OrderManagerHome.FORM_ADD_NEW_STATUS_TYPE) {
                    this.menuActionIntializer(OrderManagerHome.FORM_QUICK_NEW_ORDER, 220);
                }
                if (id === "advanceReport") {
                    this.menurReportsActionIntializer(OrderManagerHome.REPORT_DAILY_ADVANCE);
                }
                if (id === "updateNewOrder") {
                    this.menuUpdateActionInitializer(OrderManagerHome.UPDATE_NEW_ORDER,100);
                }
                if (id === "addadvance") {
                    this.menuUpdateActionInitializer(OrderManagerHome.UPDATE_ADVANCE,100);
                }
                if (id === "addNewLocation") {
                    this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_LOCATION,220);
                }
                if (id === "addNewEmployee") {
                    this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_EMPLOYEE,180);
                }
            });
        }

    }
}
