/// <reference path="..//Utility/Utility.ts"/>
/// <reference path="..//Utility/UpdateUtility.ts"/>
/// <reference path="../Utility/BulkUpdate.ts"/>
/// <reference path="../Utility/ReportingUtility.ts"/>
/// <reference path="../Utility/OrderScheduler.ts"/>
declare var Commands: any;
declare var progressOffCustom: any;
declare var dhtmlXLayoutObject;
declare var Language: any;
declare var USER_DETAILS :any;
declare var showFailedNotificationWithICON: any;
declare var jQuery: any;
declare var dhtmlXChart: any;
module com.ordermanager.home {
    export class CommandHandler {

        public static CODE_FORM_NEW_ITEM = "ANI";
        public static CODE_FORM_NEW_USER = "ANU";
        public static CODE_FORM_NEW_ACCOUNT = "ANA";
        public static CODE_FORM_NEW_ACCOUNT_SUBTYPE = "ANAST";
        public static CODE_FORM_NEW_ACCOUNT_TRANSACTION = "ANT";
        public static CODE_FORM_NEW_ORDER = "ANO";
        public static CODE_ADD_NEW_STATUS_TYPE = "ANST";
        public static CODE_QUICK_NEW_ORDER = "AQA"
        public static CODE_QUICK_NEW_LOCATION = "ANL";
        public static CODE_ADD_NEW_EMPLOYEE = "ANE";
        public static CODE_UPLOAD = "UIMG";
        public static CODE_REPORT_DAILY_ADVANCE = "RDA";
        public static CODE_REPORT_PROUCTION_REPORT = "RDP";
        public static CODE_WAGE_PAYMENT_SYSTEM = "WPS";
        public static CODE_UPDATE_NEW_ORDER = "UNO";
        public static CODE_UPDATE_ITEM = "UI"
        public static CODE_UPDATE_ADVANCE = "UAD";
        public static CODE_UPDATE_ASSIGNEMENT_WAGE = "UAW";
        public static CODE_UPDATE_DELIVERY_COMPLETED_TRANSACTION = "DP";
        public static CODE_UPDATE_LABOUR_WAGE = "CLW";
        public static CODE_BULK_UPDATE_MASTER_TAILOR_ASSIGNMENT = "UBMT";
        public static CODE_BULK_UPDATE_SINGLE_ASSIGNMENT = "UBS";
        public static CODE_BULK_UPDATE_READY_TO_DELIVER = "UBRD";
        public static CODE_BULK_CHANGE_ASSIGNMENT = "UBCA";
        public static CODE_CANCEL_ORDER = "COD";
        public static CODE_REPORT_ORDER_SCHEDULER = "ROS";
        public static CODE_REPORT_DELIVERY_TRANSACTIONS = "RDT";
    }
    export class OrderManagerHome {
        public static FORM_NEW_ITEM = "addNewItem";
        public static FORM_NEW_USER = "addNewUser";
        public static FORM_NEW_ORDER = "addNewOrder";
        public static FORM_QUICK_NEW_ORDER = "quickNewOrder";
        public static FORM_ADD_NEW_STATUS_TYPE = "addNewStatusType";
        public static FORM_ADD_NEW_LOCATION = "addNewLocation";
        public static FORM_ADD_NEW_EMPLOYEE = "addNewEmployee";
        public static FORM_ADD_NEW_ACCOUNT = "addNewAccount";
        public static FORM_ADD_NEW_ACCOUNT_SUBTYPE = "addNewAccountSubType";
        public static FORM_ADD_NEW_ACCOUNT_TRANSACTION = "addNewAccountTransaction";
        public static REPORT_DAILY_ADVANCE = "advanceReport";
        public static REPORT_ORDER_SCHEDULER = "orderScheduler";
        public static REPORT_DELIVERY_TRANSACTIONS = "deliveryTransactionsReport";
        public static UPDATE_NEW_ORDER = "updateNewOrder";
        public static UPDATE_ITEM = "updateItem";
        public static UPDATE_ADVANCE = "addadvance";
        public static UPDATE_DELIVERY_COMPLETED_TRANSACTION = "updateDeliveryCompleted";
        public static UPDATE_LABOUR_WAGE = "updateLabourWage";
        public static UPDATE_BULK_CHANGE_ASSIGNMENT = "assignmentStatusChange";
        public static UPDATE_BULK_MASTER_TAILOR = "updateBulkMasterTailor";
        public static UPDATE_BULK_READY_TO_DELIVER = "updateBulkReadyToDeliver";
        public static UPDATE_BULK_SINGLE = "updateBulkToSingle";

        
        public HomeLayoutObject: any;
        public HomeToolbar: any;
        public MenuAccordionObj: any;
        public MenuGrid: any;
        public FormEntryManagerObject: any;
        public ReportViewManagerObject: any;
        public UpdateManagerObject: any;
        public DBConnection: any;
        public ChartLayout: any;

        public AutoCompleteCommandMenu() {
            Commands = [
                { "label": "ANI   [Add New Item]", "value": "ANI" },
                { "label": "ANST [Add New Status Type]", "value": "ANST" },
                { "label": "ANA [Add New Account]", "value": "ANA" },
                { "label": "ANT [Add New Account Transaction]", "value": "ANT" },
                { "label": "ANO  [Add New Order]", "value": "ANO" },
                { "label": "ANU  [Add New User]", "value": "ANU" },
                { "label": "ANE  [Add New Employee]", "value": "ANE" },
                { "label": "ANAST  [Add New Account Subtype]", "value": "ANAST" },
                { "label": "AQA  [Add Quick Advance]", "value": "AQA" },
                { "label": "UIMG [Upload Order Image]", "value": "UIMG" },
                { "label": "ANL  [Add New Location]", "value": "ANL" },
                { "label": "DP   [Delivery Payment]", "value": "DP" },
                { "label": "RDA  [Report Daily Advance]", "value": "RDA" },
                { "label": "RDP  [Report Daily Production]", "value": "RDP" },
                { "label": "RDT  [Report Delivery Transaction]", "value": "RDT" },
                { "label": "CLW  [Change Labour Wage]", "value": "CLW" },
                { "label": "UAW  [Update Assignment Wage]", "value": "UAW" },
                { "label": "UAD  [Update Advance]", "value": "UAD" },
                { "label": "UNO  [Update New Order]", "value": "UNO" },
                { "label": "UNO  [Update Item]", "value": "UI" },
                { "label": "ROS  [Report Order Scheduler]", "value": "ROS" },
                { "label": "COD  [Cancel Order]", "value": "COD" },
                { "label": "UBCA [Update Bulk Change Assignment]", "value": "UBCA" },
                { "label": "UBRD [Update Bulk Ready to Delivery]", "value": "UBRD" },
                { "label": "UBS  [Update Bulk Single Assignment]", "value": "UBS" },
                { "label": "UBMT [Update Bulk Master Tailor Assignment]", "value": "UBMT" },
                { "label": "WPS  [Wage Payment System]", "value": "WPS" }
            ];
            $("#searchCode").autocomplete({
                source: Commands,
                select: function (event, ui) {
                }
            });
        }
        constructor() {
            this.initLayout();
            this.commandRegister();
            this.HomeLayoutObject.attachEvent("onCollapse", (name) => {
                if (name === "a") {
                    this.commandRegister();
                    this.AutoCompleteCommandMenu();
                }
            });
            this.HomeLayoutObject.attachEvent("onExpand", (name) => {
                if (name === "a") {
                    this.commandRegister();
                    this.AutoCompleteCommandMenu();
                }
            });
            this.AutoCompleteCommandMenu();
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
                else if (command.trim().toUpperCase() === CommandHandler.CODE_ADD_NEW_EMPLOYEE) {
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
                else if (command.trim().toUpperCase() === CommandHandler.CODE_FORM_NEW_ACCOUNT) {
                    this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_ACCOUNT, 220);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_FORM_NEW_ACCOUNT_TRANSACTION) {
                    this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_ACCOUNT_TRANSACTION, 220);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_FORM_NEW_ACCOUNT_SUBTYPE) {
                    this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_ACCOUNT_SUBTYPE, 220);
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
                else if (command.trim().toUpperCase() === CommandHandler.CODE_REPORT_DELIVERY_TRANSACTIONS) {
                    this.menurReportsActionIntializer(OrderManagerHome.REPORT_DELIVERY_TRANSACTIONS);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_UPDATE_NEW_ORDER) {
                    this.menuUpdateActionInitializer(OrderManagerHome.UPDATE_NEW_ORDER, 100);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_UPDATE_ITEM) {
                    this.menuUpdateActionInitializer(OrderManagerHome.UPDATE_ITEM, 100);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_UPDATE_ADVANCE) {
                    this.menuUpdateActionInitializer(OrderManagerHome.UPDATE_ADVANCE, 100);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_UPDATE_DELIVERY_COMPLETED_TRANSACTION) {
                    this.menuBulkUpdateActionInitializer(OrderManagerHome.UPDATE_DELIVERY_COMPLETED_TRANSACTION, 100);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_BULK_CHANGE_ASSIGNMENT) {
                    this.menuBulkUpdateActionInitializer(OrderManagerHome.UPDATE_BULK_CHANGE_ASSIGNMENT, 170);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_BULK_UPDATE_MASTER_TAILOR_ASSIGNMENT) {
                    this.menuBulkUpdateActionInitializer(OrderManagerHome.UPDATE_BULK_MASTER_TAILOR, 100);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_BULK_UPDATE_SINGLE_ASSIGNMENT) {
                    this.menuBulkUpdateActionInitializer(OrderManagerHome.UPDATE_BULK_SINGLE, 100);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_BULK_UPDATE_READY_TO_DELIVER) {
                    this.menuBulkUpdateActionInitializer(OrderManagerHome.UPDATE_BULK_READY_TO_DELIVER, 100);
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_REPORT_PROUCTION_REPORT) {
                    this.HomeLayoutObject.cells("a").collapse();
                    new com.ordermanager.reportingutility.DayWiseProductionReport(this.HomeLayoutObject.cells("b"), this.HomeLayoutObject.cells("c"));
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_WAGE_PAYMENT_SYSTEM) {
                    this.HomeLayoutObject.cells("a").collapse();
                    new com.ordermanager.reportingutility.WagePaymentSystem(this.HomeLayoutObject.cells("b"), this.HomeLayoutObject.cells("c"));
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_UPLOAD) {
                    this.webcamImageManager();
                }
                else if (command.trim().toUpperCase() === CommandHandler.CODE_REPORT_ORDER_SCHEDULER) {
                    this.HomeLayoutObject.cells("a").collapse();
                    this.ReportViewManagerObject = new com.ordermanager.OrderScheduler.OrderScheduler(this.HomeLayoutObject.cells("b"), this.HomeLayoutObject.cells("c"));
                    this.HomeLayoutObject.cells("c").showHeader();
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
            var opts = [
                ['lang', 'obj', "LANGUAGE : "+ USER_DETAILS.LANGUAGE, "resources/Images/edit.png"],
                ['sep01', 'sep', '', ''],
                ['lan', 'obj', "ROLE : "+ USER_DETAILS.USER_TYPE , "resources/Images/lock.png"],
                ['sep02', 'sep', '', ''],
                ['logout', 'obj', 'LOGOUT', "resources/Images/logout.png"],
               
               
                
            ];
            //   console.log("Loading : " + JSON.stringify(Language));
            this.HomeLayoutObject.progressOn();
            this.HomeLayoutObject.cells("a").setText(Language.menu + "<span>&nbsp;&nbsp;<input type='text' id='searchCode' placeholder='Shortcut Command'/></span>");
            this.HomeToolbar = this.HomeLayoutObject.attachToolbar();
            this.HomeToolbar.addText("appname", 1, "<span style='font-weight:bold'>Mallick Dresses Order Manager 1.0</span>");
            this.HomeToolbar.addButtonTwoState("dbState", 2, "Getting connectivity status ..", "resources/Images/connected.png", "resources/Images/not_connected.png");
            this.HomeToolbar.disableItem("dbState");

            this.HomeToolbar.addButtonSelect("id", 3, USER_DETAILS.USERNAME, opts, "resources/Images/user.png", "resources/Images/connected.png");
           
           
            this.HomeToolbar.addSpacer("appname");
            this.dbStatusLoader();

            this.HomeToolbar.attachEvent("onClick", (id)=>{
                if(id === "logout"){
                    window.open("/Logout","_self");
                }
            });
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
            this.ChartLayout = this.HomeLayoutObject.cells("b").attachLayout({
                pattern: "4J",
                cells: [
                    { id: "a", text: "Daily Sales", true: false },
                    { id: "b", text: "Existing Data", header: false },
                    { id: "c", text: "Existing Data", header: false },
                    { id: "d", text: "Existing Data", header: false }
                ]
            });
            //com.ordermanager.utilty.MainUtility.getImageViewer(this.ChartLayout.cells("b"),"ORDERS","1250",235,200,400);
           
        }
       
        public dbStatusLoader() {
            //            var intervalVar = setInterval(() => {
            //                try {
            //                    var currentRequest = null;
            //                    currentRequest = jQuery.ajax({
            //                        type: 'POST',
            //                        data: '',
            //                        url: 'getdBStatus',
            //                        beforeSend:  ()=> {
            //                            if (currentRequest != null) {
            //                                currentRequest.abort();
            //                                this.HomeToolbar.disableItem("dbState");
            //                                this.HomeToolbar.setItemText("dbState", "Disconnected");
            //                            }
            //                        },
            //                        success:  (data)=> {
            //                            if (data == true) {
            //                                this.HomeToolbar.enableItem("dbState");
            //                                this.HomeToolbar.setItemText("dbState", "Connected");
            //                            }
            //                            else {
            //                                this.HomeToolbar.disableItem("dbState");
            //                                this.HomeToolbar.setItemText("dbState", "Disconnected");
            //                            }
            //                        },
            //                        error:  (e)=> {
            //                            this.HomeToolbar.disableItem("dbState");
            //                            this.HomeToolbar.setItemText("dbState", "Disconnected");
            //                        }
            //                    });
            //                } catch (e) {
            //                }
            //            }, 30*1000);
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
        public menuUpdateActionInitializer(actionName, QueryFormHeight) {
            this.HomeLayoutObject.cells("a").expand();
            this.UpdateManagerObject = new com.ordermanager.UpdateUtility.UpdateUtility(actionName, this.HomeLayoutObject.cells("b"), this.HomeLayoutObject.cells("c"), QueryFormHeight);
            this.HomeLayoutObject.cells("c").showHeader();

        }
        public menuBulkUpdateActionInitializer(actionName, QueryFormHeight) {
            this.HomeLayoutObject.cells("a").collapse();
            this.UpdateManagerObject = new com.ordermanager.bulkupdate.BulkUpdate(actionName, this.HomeLayoutObject.cells("b"), this.HomeLayoutObject.cells("c"), QueryFormHeight, 5);
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
                    this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_STATUS_TYPE, 220);
                }
                if (id === "advanceReport") {
                    this.menurReportsActionIntializer(OrderManagerHome.REPORT_DAILY_ADVANCE);
                }
                if (id === "updateNewOrder") {
                    this.menuUpdateActionInitializer(OrderManagerHome.UPDATE_NEW_ORDER, 100);
                }
                if (id === "addadvance") {
                    this.menuUpdateActionInitializer(OrderManagerHome.UPDATE_ADVANCE, 100);
                }
                if (id === "addNewLocation") {
                    this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_LOCATION, 220);
                }
                if (id === "addNewEmployee") {
                    this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_EMPLOYEE, 180);
                }
            });
        }
        public webcamImageManager() {
            var WindowObject = com.ordermanager.utilty.MainUtility.getModelWindow("Take Product Image", 580, 580).attachURL("resources/JS_WEBCAM/Camera.html?KEY=32434&MODULE=ORDERS");
        }
     

    }
}
