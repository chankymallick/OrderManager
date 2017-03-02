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
            CommandHandler.CODE_REPORT_DAILY_ADVANCE = "RDA";
            CommandHandler.CODE_UPDATE_NEW_ORDER = "UNO";
            CommandHandler.CODE_UPDATE_ADVANCE = "UAD";
            CommandHandler.CODE_BULK_UPDATE_MASTER_TAILOR_ASSIGNMENT = "UBMT";
            CommandHandler.CODE_BULK_UPDATE_READY_TO_DELIVER = "UBRD";
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
                        if (command.trim().toUpperCase() === CommandHandler.CODE_ADD_NEW_EMPLOYEE) {
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
                    this.MenuAccordionObj.attachEvent("onActive", function (id, state) {
                        _this.loadMenuItems(id);
                    });
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
                            _this.menuActionIntializer(OrderManagerHome.FORM_QUICK_NEW_ORDER, 220);
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
                return OrderManagerHome;
            }());
            OrderManagerHome.FORM_NEW_ITEM = "addNewItem";
            OrderManagerHome.FORM_NEW_USER = "addNewUser";
            OrderManagerHome.FORM_NEW_ORDER = "addNewOrder";
            OrderManagerHome.FORM_QUICK_NEW_ORDER = "quickNewOrder";
            OrderManagerHome.FORM_ADD_NEW_STATUS_TYPE = "addNewStatusType";
            OrderManagerHome.FORM_ADD_NEW_LOCATION = "addNewLocation";
            OrderManagerHome.FORM_ADD_NEW_EMPLOYEE = "addNewEmployee";
            OrderManagerHome.REPORT_DAILY_ADVANCE = "advanceReport";
            OrderManagerHome.UPDATE_NEW_ORDER = "updateNewOrder";
            OrderManagerHome.UPDATE_ADVANCE = "addadvance";
            OrderManagerHome.UPDATE_BULK_MASTER_TAILOR = "updateBulkMasterTailor";
            OrderManagerHome.UPDATE_BULK_READY_TO_DELIVER = "updateBulkReadyToDeliver";
            home.OrderManagerHome = OrderManagerHome;
        })(home = ordermanager.home || (ordermanager.home = {}));
    })(ordermanager = com.ordermanager || (com.ordermanager = {}));
})(com || (com = {}));
