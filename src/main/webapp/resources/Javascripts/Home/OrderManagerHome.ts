/// <reference path="..//Utility/Utility.ts"/>
declare var progressOffCustom: any;
declare var dhtmlXLayoutObject;
declare var Language:any;
module com.ordermanager.home {
    export class CommandHandler{
      public static CODE_FORM_NEW_ITEM = "ANI";
    }
    export class OrderManagerHome {
        public static FORM_NEW_ITEM = "loadNewOrderItemForm";
        public static FORM_NEW_USER = "addNewUser";
        public HomeLayoutObject: any;
        public HomeToolbar: any;
        public MenuAccordionObj: any;
        public MenuGrid: any;
        public FormEntryManagerObject: any;
        constructor() {
            this.initLayout();
            this.commandRegister();
            this.HomeLayoutObject.attachEvent("onCollapse", (name)=>{
                if(name === "a"){
                  this.commandRegister();
                }
            });
            this.HomeLayoutObject.attachEvent("onExpand", (name)=>{
                if(name === "a"){
                  this.commandRegister();
                }
            });
        }
        public commandRegister(){
          shortcut.add("Home", () => {
          document.getElementById("searchCode").focus();
          }, {
                'type': 'keyup',
                'disable_in_input': false,
                'target': document,
                'propagate': true
            });
          shortcut.add("Enter", () => {
          var command = document.getElementById("searchCode").value;
          if(command.trim().toUpperCase() === CommandHandler.CODE_FORM_NEW_ITEM){
                    this.menuActionIntializer(OrderManagerHome.FORM_NEW_ITEM);
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
            this.HomeLayoutObject.cells("a").setText(Language.menu+"  <span>&nbsp;&nbsp;<input type='text' id='searchCode' placeholder='Shortcut Command'/></span>");
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
        public menuActionIntializer(actionName){
          this.FormEntryManagerObject = new com.ordermanager.utilty.FormEntryManager(this.HomeLayoutObject.cells("b"), this.HomeLayoutObject.cells("c"), actionName, 0, 200);
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
            this.MenuGrid.init();
            this.MenuGrid.attachEvent("onXLE", () => {
                progressOffCustom(this.HomeLayoutObject);
            });
            this.MenuGrid.load("LoadMenuItems?menutype=" + itemtype);
            this.MenuGrid.attachEvent("onRowSelect", (id, ind) => {
                if (id === "AddNewItem") {
                  this.menuActionIntializer(OrderManagerHome.FORM_NEW_ITEM);
                }
                if (id === "addnewuser") {
                  this.menuActionIntializer(OrderManagerHome.FORM_NEW_USER);
                }

            });
        }

    }
}
