var com;
(function (com) {
    var ordermanager;
    (function (ordermanager) {
        var home;
        (function (home) {
            var CommandHandler = (function () {
                function CommandHandler() {
                }
                CommandHandler.CODE_FORM_NEW_ITEM = "ANI";
                CommandHandler.CODE_FORM_NEW_USER = "ANU";
                CommandHandler.CODE_FORM_NEW_ORDER = "ANO";
                return CommandHandler;
            }());
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
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_FORM_NEW_USER) {
                            _this.menuActionIntializer(OrderManagerHome.FORM_NEW_USER, 200);
                        }
                        else if (command.trim().toUpperCase() === CommandHandler.CODE_FORM_NEW_ORDER) {
                            _this.menuActionIntializer(OrderManagerHome.FORM_NEW_ORDER, 170);
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
                    this.HomeLayoutObject.cells("a").setText(Language.menu + "  <span>&nbsp;&nbsp;<input type='text' id='searchCode' placeholder='Shortcut Command'/></span>");
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
                    this.FormEntryManagerObject = new com.ordermanager.utilty.FormEntryManager(this.HomeLayoutObject.cells("b"), this.HomeLayoutObject.cells("c"), actionName, 0, dataviewcellheight);
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
                    });
                };
                OrderManagerHome.FORM_NEW_ITEM = "addNewItem";
                OrderManagerHome.FORM_NEW_USER = "addNewUser";
                OrderManagerHome.FORM_NEW_ORDER = "addNewOrder";
                return OrderManagerHome;
            }());
            home.OrderManagerHome = OrderManagerHome;
        })(home = ordermanager.home || (ordermanager.home = {}));
    })(ordermanager = com.ordermanager || (com.ordermanager = {}));
})(com || (com = {}));
