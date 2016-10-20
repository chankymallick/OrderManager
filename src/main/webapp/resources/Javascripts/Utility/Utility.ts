declare var progressOffCustom:any;
declare var shortcut:any;
declare var showSuccessNotification:any;
declare var showFailedNotification:any;
declare var SynchronousGetAjaxRequest:any;
declare var showSuccessNotificationWithICON:any;
declare var showFailedNotificationWithICON:any;
module com.ordermanager.utilty{
  export class MainUtility{


  }
  export class FormEntryManager{
    public DataEntryLayoutCell:any;
    public NotificationCell:any;
    public ModifiedLayoutObject:any;
    public FormName:any;
    public FormObject:any
    public MainUtilityObj:MainUtility;
    public OperationToolbar:any;
    public DataEntryLayoutCellHeight:any;
    public DataViewLayoutCellHeight:any;
    public DataTabber:any;
    public DataViewGridObject:any;
    constructor(layoutCell:any,notificationCell:any,formname:any,dataentryCellHeight:any,dataviewcellheight:any){
    this.DataEntryLayoutCellHeight = dataentryCellHeight;
    this.DataViewLayoutCellHeight = dataviewcellheight;
    this.DataEntryLayoutCell = layoutCell;
    this.NotificationCell = notificationCell;
    this.FormName=formname;
    this.constructInnerLayoutforDataEntry();
    this.MainUtilityObj = new com.ordermanager.utilty.MainUtility();
    this.shortCutRegister();
    }
    public shortCutRegister(){
      shortcut.add("F8",()=> {
      this.validateAndSaveFormData();
      },{
      	'type':'keyup',
        'disable_in_input':false,
        'target':document,
      	'propagate':true
      });
    }

    public constructInnerLayoutforDataEntry(){
    this.ModifiedLayoutObject = this.DataEntryLayoutCell.attachLayout({
    pattern: "2E",
    cells: [
      { id: "a", text: "Data",header:false },
      { id: "b", text: "Existing Data",header:false, height:this.DataViewLayoutCellHeight}
          ]
      });
      this.ModifiedLayoutObject.progressOn();
      this.DataTabber = this.ModifiedLayoutObject.cells("a").attachTabbar();
      this.DataTabber.addTab("data", "Data Entry", null, null, true, false);
      this.DataTabber.addTab("default", "Set Default Data", null, null, false, false);
      this.FormInitialization();
      this.FormObject.attachEvent("onXLE",()=>{
        progressOffCustom(this.ModifiedLayoutObject);
      });
      this.OperationToolbar = this.ModifiedLayoutObject.attachToolbar();
      this.OperationToolbar.loadStruct("operationToolbar","json");
      this.OperationToolbar.attachEvent("onClick", (id)=>{
        if(id === "clear"){
        this.FormObject.clear();
        }
        if(id === "new"){
        this.setFormStateNewRecord();
        }
        if(id === "save"){
        this.validateAndSaveFormData();
        }
      });
      // this.FormObject.attachEvent("onKeyUp",(inp, ev, name, value)=>{
      // });
      this.DataViewGridObject=this.ModifiedLayoutObject.cells("b").attachGrid();
      this.DataViewGridObject.load("LoadDataViewGrid?gridname="+this.FormName);
      this.DataViewGridObject.attachEvent("onRowSelect",(id,ind) =>{
      });


    }
    public FormInitialization(){
      if(this.FormObject != null || this.FormObject != undefined ){
        this.FormObject.unload();
      }
      this.FormObject=this.DataTabber.tabs("data").attachForm();
      this.FormObject.load(this.FormName);
      this.FormObject.enableLiveValidation(true);
    }
    public setFormStateAfterSave(){
      this.FormObject.lock();
      this.OperationToolbar.enableItem("new");
      this.OperationToolbar.disableItem("save");
      this.OperationToolbar.disableItem("clear");
      this.DataViewGridObject.load("LoadDataViewGrid?gridname="+this.FormName);
      shortcut.add("F4",()=> {
      this.setFormStateNewRecord();
      },{
        'type':'keyup',
        'disable_in_input':false,
        'target':document,
        'propagate':true
      });
      shortcut.remove("F8");
    }
    public setFormStateNewRecord(){
          this.FormInitialization();
          this.OperationToolbar.disableItem("new");
          this.OperationToolbar.enableItem("save");
          this.OperationToolbar.enableItem("clear");
          shortcut.remove("F4");
          shortcut.add("F8",()=> {
          this.validateAndSaveFormData();
          },{
          	'type':'keyup',
            'disable_in_input':false,
            'target':document,
          	'propagate':true
          });
    }
    public validateAndSaveFormData(){
         this.setSpecificBeforeSave();
         if(this.FormObject.validate()){
             this.FormObject.updateValues();
             this.DataEntryLayoutCell.progressOn();
             var Response = SynchronousGetAjaxRequest("addItem?ParamData="+JSON.stringify(this.FormObject.getValues()),"",null);
             if(Response.RESPONSE_STATUS === "SUCCESS"){
             showSuccessNotificationWithICON(Response.RESPONSE_MESSAGE);
             this.setFormStateAfterSave();
             this.NotificationCell.collapse();
             this.DataEntryLayoutCell.progressOff();
             }
             if(Response.RESPONSE_STATUS === "FAILED"){
             showFailedNotificationWithICON(Response.RESPONSE_MESSAGE);
             this.NotificationCell.attachHTMLString("<b style='color:red'>"+Response.RESPONSE_VALUE.EXCEPTION_MESSAGE+"</b>");
             this.NotificationCell.expand();
             this.DataEntryLayoutCell.progressOff();
             }
         }
         else
         {
         showFailedNotification("Data Validation Error")
         }
    }
    public setSpecificFormSettingsoNLoad(){
      if(this.FormName === "loadNewOrderItemForm"){

      }
    }
      public setSpecificBeforeSave(){
        if(this.FormName === "loadNewOrderItemForm"){

        }
    }
    public setSpecificAfterSave(){
      if(this.FormName === "loadNewOrderItemForm"){

      }
    }
  }
}
