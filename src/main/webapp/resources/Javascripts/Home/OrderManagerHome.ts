declare var dhtmlXLayoutObject;
module com.ordermanager.home{
export class OrderManagerHome{
    public HomeLayoutObject:any;
    public HomeToolbar:any;
    public MenuAccordionObj:any;
    public MenuGrid:any;
    constructor(){

      this.initLayout();

    }
    public initLayout(){
      this.HomeLayoutObject = new dhtmlXLayoutObject({
                parent : "Layout_Container",
                pattern : "3L",
                cells:
                [
                {id : "a",text : "Menu",width : 250 },
                {id : "b",text : "Dashboard"},
                {id : "c",text : "Notifications",height:150}
                ]
              });
      this.HomeLayoutObject.progressOn();
      this.HomeLayoutObject.cells("a").setText("Menu  <span>&nbsp;&nbsp;<input type='text' id='searchCode' placeholder='Shortcut Command'/></span>");
      this.HomeToolbar = this.HomeLayoutObject.attachToolbar();
      this.HomeToolbar.addText("appname", 1, "<span style='font-weight:bold'>Mallick Dresses Order Manager 1.0</span>");
      this.MenuAccordionObj = this.HomeLayoutObject.cells("a").attachAccordion();
      this.HomeLayoutObject.cells("a").showHeader();
      this.MenuAccordionObj.addItem("ordersandbills", "Order & Bill");
			this.MenuAccordionObj.addItem("mastertailor", "Master & Tailors");
			this.MenuAccordionObj.addItem("labourmanager", "Labour Manager");
      this.MenuAccordionObj.addItem("salesmanmanager", "Salesman Manager");
      this.MenuAccordionObj.addItem("usermanager", "User Management");
      this.MenuAccordionObj.addItem("supplierbook", "Suppliers Books");
      this.MenuGrid = this.MenuAccordionObj.cells("ordersandbills").attachGrid();
      this.loadMenuItems("ordersandbills");
      this.MenuAccordionObj.attachEvent("onActive", (id, state)=>{
            this.loadMenuItems(id)
      });
      this.HomeLayoutObject.progressOff();
    }
    public skinChanger(dhtmlxObject:any,skinType:any){
         if(skinType === "white"){
         dhtmlxObject.setSkin("dhx_web");
         }
    }
    public loadMenuItems(itemtype : any){
      if(this.MenuGrid != null || this.MenuGrid === undefined ){
        this.MenuGrid.destructor();
      }
      this.MenuGrid = this.MenuAccordionObj.cells(itemtype).attachGrid();
      this.MenuGrid.setHeader("image,name");
      this.MenuGrid.setNoHeader(true);
      this.MenuGrid.setInitWidths("50,180");
      this.MenuGrid.setColAlign("left,left");
      this.MenuGrid.setColTypes("ro,ro");
      this.MenuGrid.init();
      this.MenuGrid.load("LoadMenuItems?menutype="+itemtype);
    }

    }
}
