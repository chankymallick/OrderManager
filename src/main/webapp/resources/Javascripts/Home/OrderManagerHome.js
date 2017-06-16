/// <reference path="..//Utility/Utility.ts"/>
/// <reference path="..//Utility/UpdateUtility.ts"/>
/// <reference path="../Utility/BulkUpdate.ts"/>
/// <reference path="../Utility/ReportingUtility.ts"/>
/// <reference path="../Utility/OrderScheduler.ts"/>
var com;
(function (com) {
    var ordermanager;
    (function (ordermanager) {
        var home;
        (function (home) {
            var CommandHandler = (function () {
                function CommandHandler() {
                }
                return CommandHandler;
            }());
            CommandHandler.CODE_FORM_NEW_ITEM = "ANI";
            CommandHandler.CODE_FORM_NEW_USER = "ANU";
            CommandHandler.CODE_FORM_NEW_ORDER = "ANO";
            CommandHandler.CODE_ADD_NEW_STATUS_TYPE = "ANST";
            CommandHandler.CODE_QUICK_NEW_ORDER = "AQA";
            CommandHandler.CODE_QUICK_NEW_LOCATION = "ANL";
            CommandHandler.CODE_ADD_NEW_EMPLOYEE = "ANE";
            CommandHandler.CODE_UPLOAD = "UIMG";
            //----------------------------------------
            CommandHandler.CODE_REPORT_DAILY_ADVANCE = "RDA";
            CommandHandler.CODE_REPORT_PROUCTION_REPORT = "RDP";
            CommandHandler.CODE_WAGE_PAYMENT_SYSTEM = "WPS";
            //----------------------------------------
            CommandHandler.CODE_UPDATE_NEW_ORDER = "UNO";
            CommandHandler.CODE_UPDATE_ADVANCE = "UAD";
            CommandHandler.CODE_BULK_UPDATE_MASTER_TAILOR_ASSIGNMENT = "UBMT";
            CommandHandler.CODE_BULK_UPDATE_READY_TO_DELIVER = "UBRD";
            CommandHandler.CODE_REPORT_ORDER_SCHEDULER = "ROS";
            home.CommandHandler = CommandHandler;
            var OrderManagerHome = (function () {
                function OrderManagerHome() {
                    var _this = this;
                    this.initLayout();
                    this.commandRegister();
                    this.HomeLayoutObject.attachEvent("onCollapse", function (name) {
                        if (name === "a") {
                            _this.commandRegister();
                        }
                    });
                    this.HomeLayoutObject.attachEvent("onExpand", function (name) {
                        if (name === "a") {
                            _this.commandRegister();
                        }
                    });
                }
                OrderManagerHome.prototype.commandRegister = function () {
                    var _this = this;
                    shortcut.add("Home", function () {
                        document.getElementById("searchCode").value = "";
                        document.getElementById("searchCode").focus();
                    }, {
                        'type': 'keyup',
                        'disable_in_input': false,
                        'target': document,
                        'propagate': true
                    });
                    shortcut.add("Enter", function () {
                        var command = document.getElementById("searchCode").value;
                        if (command.trim().toUpperCase() === CommandHandler.CODE_FORM_NEW_ITEM) {
                            _this.menuActionIntializer(OrderManagerHome.FORM_NEW_ITEM, 200);
                        }
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_ADD_NEW_EMPLOYEE) {
                            _this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_EMPLOYEE, 200);
                        }
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_FORM_NEW_USER) {
                            _this.menuActionIntializer(OrderManagerHome.FORM_NEW_USER, 200);
                        }
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_FORM_NEW_ORDER) {
                            _this.menuActionIntializer(OrderManagerHome.FORM_NEW_ORDER, 170);
                        }
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_QUICK_NEW_ORDER) {
                            _this.menuActionIntializer(OrderManagerHome.FORM_QUICK_NEW_ORDER, 220);
                        }
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_ADD_NEW_STATUS_TYPE) {
                            _this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_STATUS_TYPE, 220);
                        }
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_QUICK_NEW_LOCATION) {
                            _this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_LOCATION, 220);
                        }
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_REPORT_DAILY_ADVANCE) {
                            _this.menurReportsActionIntializer(OrderManagerHome.REPORT_DAILY_ADVANCE);
                        }
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_UPDATE_NEW_ORDER) {
                            _this.menuUpdateActionInitializer(OrderManagerHome.UPDATE_NEW_ORDER, 100);
                        }
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_UPDATE_ADVANCE) {
                            _this.menuUpdateActionInitializer(OrderManagerHome.UPDATE_ADVANCE, 100);
                        }
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_BULK_UPDATE_MASTER_TAILOR_ASSIGNMENT) {
                            _this.menuBulkUpdateActionInitializer(OrderManagerHome.UPDATE_BULK_MASTER_TAILOR, 100);
                        }
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_BULK_UPDATE_READY_TO_DELIVER) {
                            _this.menuBulkUpdateActionInitializer(OrderManagerHome.UPDATE_BULK_READY_TO_DELIVER, 100);
                        }
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_REPORT_PROUCTION_REPORT) {
                            _this.HomeLayoutObject.cells("a").collapse();
                            new com.ordermanager.reportingutility.DayWiseProductionReport(_this.HomeLayoutObject.cells("b"), _this.HomeLayoutObject.cells("c"));
                        }
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_WAGE_PAYMENT_SYSTEM) {
                            _this.HomeLayoutObject.cells("a").collapse();
                            new com.ordermanager.reportingutility.WagePaymentSystem(_this.HomeLayoutObject.cells("b"), _this.HomeLayoutObject.cells("c"));
                        }
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_UPLOAD) {
                            _this.webcamImageManager();
                        }
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_REPORT_ORDER_SCHEDULER) {
                            _this.HomeLayoutObject.cells("a").collapse();
                            _this.ReportViewManagerObject = new com.ordermanager.OrderScheduler.OrderScheduler(_this.HomeLayoutObject.cells("b"), _this.HomeLayoutObject.cells("c"));
                            _this.HomeLayoutObject.cells("c").showHeader();
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
                };
                OrderManagerHome.prototype.initLayout = function () {
                    var _this = this;
                    this.HomeLayoutObject = new dhtmlXLayoutObject({
                        parent: "Layout_Container",
                        pattern: "3L",
                        cells: [
                            { id: "a", text: "Menu", width: 250 },
                            { id: "b", text: "Dashboard" },
                            { id: "c", text: "Notifications", height: 150, collapse: true }
                        ]
                    });
                    //   console.log("Loading : " + JSON.stringify(Language));
                    this.HomeLayoutObject.progressOn();
                    this.HomeLayoutObject.cells("a").setText(Language.menu + "<span>&nbsp;&nbsp;<input type='text' id='searchCode' placeholder='Shortcut Command'/></span>");
                    this.HomeToolbar = this.HomeLayoutObject.attachToolbar();
                    this.HomeToolbar.addText("appname", 1, "<span style='font-weight:bold'>Mallick Dresses Order Manager 1.0</span>");
                    this.HomeToolbar.addButtonTwoState("dbState", 2, "Getting connectivity status ..", "resources/Images/connected.png", "resources/Images/not_connected.png");
                    this.HomeToolbar.disableItem("dbState");
                    this.HomeToolbar.addSpacer("appname");
                    this.dbStatusLoader();
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
                    this.MenuAccordionObj.attachEvent("onActive", function (id, state) {
                        _this.loadMenuItems(id);
                    });
                };
                OrderManagerHome.prototype.dbStatusLoader = function () {
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
                };
                OrderManagerHome.prototype.skinChanger = function (dhtmlxObject, skinType) {
                    if (skinType === "white") {
                        dhtmlxObject.setSkin("dhx_web");
                    }
                };
                OrderManagerHome.prototype.menuActionIntializer = function (actionName, dataviewcellheight) {
                    this.HomeLayoutObject.cells("a").expand();
                    this.FormEntryManagerObject = new com.ordermanager.utilty.FormEntryManager(this.HomeLayoutObject.cells("b"), this.HomeLayoutObject.cells("c"), actionName, 0, dataviewcellheight);
                    this.HomeLayoutObject.cells("c").showHeader();
                };
                OrderManagerHome.prototype.menuUpdateActionInitializer = function (actionName, QueryFormHeight) {
                    this.HomeLayoutObject.cells("a").expand();
                    this.UpdateManagerObject = new com.ordermanager.UpdateUtility.UpdateUtility(actionName, this.HomeLayoutObject.cells("b"), this.HomeLayoutObject.cells("c"), QueryFormHeight);
                    this.HomeLayoutObject.cells("c").showHeader();
                };
                OrderManagerHome.prototype.menuBulkUpdateActionInitializer = function (actionName, QueryFormHeight) {
                    this.HomeLayoutObject.cells("a").collapse();
                    this.UpdateManagerObject = new com.ordermanager.bulkupdate.BulkUpdate(actionName, this.HomeLayoutObject.cells("b"), this.HomeLayoutObject.cells("c"), QueryFormHeight, 5);
                    this.HomeLayoutObject.cells("c").showHeader();
                };
                OrderManagerHome.prototype.menurReportsActionIntializer = function (actionName) {
                    this.HomeLayoutObject.cells("a").collapse();
                    this.ReportViewManagerObject = new com.ordermanager.reportingutility.TransactionReports(this.HomeLayoutObject.cells("b"), this.HomeLayoutObject.cells("c"), actionName);
                    this.HomeLayoutObject.cells("c").showHeader();
                };
                OrderManagerHome.prototype.loadMenuItems = function (itemtype) {
                    var _this = this;
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
                    this.MenuGrid.attachEvent("onXLE", function () {
                        progressOffCustom(_this.HomeLayoutObject);
                    });
                    this.MenuGrid.load("LoadMenuItems?menutype=" + itemtype);
                    this.MenuGrid.attachEvent("onRowSelect", function (id, ind) {
                        if (id === "AddNewItem") {
                            _this.menuActionIntializer(OrderManagerHome.FORM_NEW_ITEM, 200);
                        }
                        if (id === "addnewuser") {
                            _this.menuActionIntializer(OrderManagerHome.FORM_NEW_USER, 200);
                        }
                        if (id === "addneworder") {
                            _this.menuActionIntializer(OrderManagerHome.FORM_NEW_ORDER, 170);
                        }
                        if (id === "quickNewOrder") {
                            _this.menuActionIntializer(OrderManagerHome.FORM_QUICK_NEW_ORDER, 220);
                        }
                        if (id === OrderManagerHome.FORM_ADD_NEW_STATUS_TYPE) {
                            _this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_STATUS_TYPE, 220);
                        }
                        if (id === "advanceReport") {
                            _this.menurReportsActionIntializer(OrderManagerHome.REPORT_DAILY_ADVANCE);
                        }
                        if (id === "updateNewOrder") {
                            _this.menuUpdateActionInitializer(OrderManagerHome.UPDATE_NEW_ORDER, 100);
                        }
                        if (id === "addadvance") {
                            _this.menuUpdateActionInitializer(OrderManagerHome.UPDATE_ADVANCE, 100);
                        }
                        if (id === "addNewLocation") {
                            _this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_LOCATION, 220);
                        }
                        if (id === "addNewEmployee") {
                            _this.menuActionIntializer(OrderManagerHome.FORM_ADD_NEW_EMPLOYEE, 180);
                        }
                    });
                };
                OrderManagerHome.prototype.webcamImageManager = function () {
                    var WindowObject = this.getModelWindow("Take Product Image", 800, 550);
                    WindowObject.attachURL("resources/JS_WEBCAM/demo/index.html?BILL_NO=9988");
                };
                OrderManagerHome.prototype.getModelWindow = function (HeaderText, Height, Width) {
                    var myWins = new dhtmlXWindows();
                    myWins.createWindow("win1", 50, 50, Height, Width);
                    myWins.window("win1").denyPark();
                    myWins.window("win1").denyResize();
                    myWins.window("win1").center();
                    myWins.window("win1").setModal(true);
                    myWins.window("win1").setText(HeaderText);
                    return myWins.window("win1");
                };
                return OrderManagerHome;
            }());
            OrderManagerHome.FORM_NEW_ITEM = "addNewItem";
            OrderManagerHome.FORM_NEW_USER = "addNewUser";
            OrderManagerHome.FORM_NEW_ORDER = "addNewOrder";
            OrderManagerHome.FORM_QUICK_NEW_ORDER = "quickNewOrder";
            OrderManagerHome.FORM_ADD_NEW_STATUS_TYPE = "addNewStatusType";
            OrderManagerHome.FORM_ADD_NEW_LOCATION = "addNewLocation";
            OrderManagerHome.FORM_ADD_NEW_EMPLOYEE = "addNewEmployee";
            //---------------------------------------------------
            OrderManagerHome.REPORT_DAILY_ADVANCE = "advanceReport";
            OrderManagerHome.REPORT_ORDER_SCHEDULER = "orderScheduler";
            //---------------------------------------------------
            OrderManagerHome.UPDATE_NEW_ORDER = "updateNewOrder";
            OrderManagerHome.UPDATE_ADVANCE = "addadvance";
            OrderManagerHome.UPDATE_BULK_MASTER_TAILOR = "updateBulkMasterTailor";
            OrderManagerHome.UPDATE_BULK_READY_TO_DELIVER = "updateBulkReadyToDeliver";
            home.OrderManagerHome = OrderManagerHome;
        })(home = ordermanager.home || (ordermanager.home = {}));
    })(ordermanager = com.ordermanager || (com.ordermanager = {}));
})(com || (com = {}));
