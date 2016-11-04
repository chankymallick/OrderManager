<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <style>
            #Layout_Container,body,html
            {
                width:100%;
                height:100%;
                overflow: hidden !important;
                margin: 0px auto;        

            }
            .DHTMLX_LABEL1{
                background-color: #1faeff;               
                line-height: 0px;
                margin-top: 15px;
                height: 17px;
                text-transform: uppercase;
            }         
            .DHTMLX_LABEL2{
                background-color: #00c9f2;               
                line-height: 0px;
                margin-top: 15px;
                height: 17px;               
            }         
            .dhxform_obj_material div.dhxform_img.btn2state_0 {
                background-image: url("resources/Images/toggle_off.png");
                width: 42px;
                height: 24px;
            }

            .dhxform_obj_material div.dhxform_img.btn2state_1 {
                background-image: url("resources/Images/toggle_on.png");
                width: 42px;
                height: 24px;
            }
            .dhtmlx-SuccessNotification{
                font-weight:bold !important;
                color:blue !important;          
            }
            .ITEM_BUTTON1 {
                -moz-box-shadow:inset 0px 1px 0px 0px #bee2f9;
                -webkit-box-shadow:inset 0px 1px 0px 0px #bee2f9;
                box-shadow:inset 0px 1px 0px 0px #bee2f9;
                background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #0068a8), color-stop(1, #000000));
                background:-moz-linear-gradient(top, #0068a8 5%, #000000 100%);
                background:-webkit-linear-gradient(top, #0068a8 5%, #000000 100%);
                background:-o-linear-gradient(top, #0068a8 5%, #000000 100%);
                background:-ms-linear-gradient(top, #0068a8 5%, #000000 100%);
                background:linear-gradient(to bottom, #0068a8 5%, #000000 100%);
                filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#0068a8', endColorstr='#000000',GradientType=0);
                background-color:#0068a8;
                -moz-border-radius:6px;
                -webkit-border-radius:6px;
                border-radius:6px;
                border:1px solid #ffffff;
                display:inline-block;
                cursor:pointer;
                color:#ffffff;
                font-family:Arial;
                font-size:15px;
                font-weight:bold;
                height:40px;
                width:220px;
                text-decoration:none;
                text-align: center;
                line-height: 3;
                margin-top: 3px;    
            }
            .ITEM_BUTTON1:hover {
                background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #000000), color-stop(1, #0068a8));
                background:-moz-linear-gradient(top, #000000 5%, #0068a8 100%);
                background:-webkit-linear-gradient(top, #000000 5%, #0068a8 100%);
                background:-o-linear-gradient(top, #000000 5%, #0068a8 100%);
                background:-ms-linear-gradient(top, #000000 5%, #0068a8 100%);
                background:linear-gradient(to bottom, #000000 5%, #0068a8 100%);
                filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#000000', endColorstr='#0068a8',GradientType=0);
                background-color:#000000;
            }
            .ITEM_BUTTON1:active {
                position:relative;
                top:1px;
            }
            .ITEM_BUTTON2 {
                background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #ffad0a), color-stop(1, #d99100));
                background:-moz-linear-gradient(top, #ffad0a 5%, #d99100 100%);
                background:-webkit-linear-gradient(top, #ffad0a 5%, #d99100 100%);
                background:-o-linear-gradient(top, #ffad0a 5%, #d99100 100%);
                background:-ms-linear-gradient(top, #ffad0a 5%, #d99100 100%);
                background:linear-gradient(to bottom, #ffad0a 5%, #d99100 100%);
                filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffad0a', endColorstr='#d99100',GradientType=0);
                background-color:#ffad0a;
                -moz-border-radius:6px;
                -webkit-border-radius:6px;
                border-radius:6px;
                border:1px solid #ffe014;
                display:inline-block;
                cursor:pointer;
                color:#ffffff;
                font-family:Arial;
                font-size:15px;
                font-weight:bold;
                height:40px;
                width:220px;
                text-decoration:none;
                text-align: center;
                line-height: 3;
                margin-top: 3px;    

            }
            .ITEM_BUTTON2:hover {
                background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #d99100), color-stop(1, #ffad0a));
                background:-moz-linear-gradient(top, #d99100 5%, #ffad0a 100%);
                background:-webkit-linear-gradient(top, #d99100 5%, #ffad0a 100%);
                background:-o-linear-gradient(top, #d99100 5%, #ffad0a 100%);
                background:-ms-linear-gradient(top, #d99100 5%, #ffad0a 100%);
                background:linear-gradient(to bottom, #d99100 5%, #ffad0a 100%);
                filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#d99100', endColorstr='#ffad0a',GradientType=0);
                background-color:#d99100;
            }
            .ITEM_BUTTON2:active {
                position:relative;
                top:1px;
            }
            .ITEM_BUTTON3 {     
                background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #0068a8), color-stop(1, #000000));
                background:-moz-linear-gradient(top, #0068a8 5%, #000000 100%);
                background:-webkit-linear-gradient(top, #0068a8 5%, #000000 100%);
                background:-o-linear-gradient(top, #0068a8 5%, #000000 100%);
                background:-ms-linear-gradient(top, #0068a8 5%, #000000 100%);
                background:linear-gradient(to bottom, #0068a8 5%, #000000 100%);
                filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#0068a8', endColorstr='#000000',GradientType=0);
                background-color:#0068a8;
                -moz-border-radius:6px;
                -webkit-border-radius:6px;
                border-radius:6px;
                border:1px solid #ffffff;
                display:inline-block;
                cursor:pointer;
                color:#ffffff;
                font-family:Arial;
                font-size:15px;
                font-weight:bold;
               height:40px;
                width:220px;
                text-decoration:none;
                text-align: center;
                line-height: 3;
                margin-top: 3px;    


            }
            .ITEM_BUTTON3:hover {
                background:-webkit-gradient(linear, left top, left bottom, color-stop(0.05, #000000), color-stop(1, #0068a8));
                background:-moz-linear-gradient(top, #000000 5%, #0068a8 100%);
                background:-webkit-linear-gradient(top, #000000 5%, #0068a8 100%);
                background:-o-linear-gradient(top, #000000 5%, #0068a8 100%);
                background:-ms-linear-gradient(top, #000000 5%, #0068a8 100%);
                background:linear-gradient(to bottom, #000000 5%, #0068a8 100%);
                filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#000000', endColorstr='#0068a8',GradientType=0);
                background-color:#000000;
            }
            .ITEM_BUTTON3:active {
                position:relative;
                top:1px;
            }
            .SUB_ITEMCSS{              
                background: #45484d;
                background: -moz-linear-gradient(top,  #45484d 0%, #000000 100%);
                background: -webkit-linear-gradient(top,  #45484d 0%,#000000 100%);
                background: linear-gradient(to bottom,  #45484d 0%,#000000 100%);
                filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#45484d', endColorstr='#000000',GradientType=0 ); 
                color:white;
                text-align: center;
                height: 40px;
                border-radius: 5px;
                line-height: 40px;
            }
            .GridHover{
                background-color: #bee2f9;
                color:white;
                cursor: pointer;
            }
           .hdrcell{
            text-align:center;
            }

        </style>
        <script src="<c:url value='/resources/Javascripts/Utility/main.js' />" ></script>
        <script src="<c:url value='/resources/Javascripts/Utility/shortcut.js' />" ></script>
        <link href="<c:url value='/resources/Javascripts/Dhtmlx/codebase/dhtmlx.css' />" type="text/css" rel="stylesheet" />
        <script src="<c:url value='/resources/Javascripts/Dhtmlx/codebase/dhtmlx.js' />" ></script>
        <script src="<c:url value='/resources/Javascripts/Utility/Utility.js' />" ></script> 
        <script src="<c:url value='/resources/Javascripts/Home/OrderManagerHome.js' />" ></script> 
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script>
            loadLanguagePack();
            dhtmlXForm.prototype.keyPlus = function () {

                var keyps = {}

                keyps.cancelEvent = function (event) {
                    if (event.preventDefault)
                        event.preventDefault();
                    else
                        event.returnValue = false;
                }


                keyps.focus = function (frm, ev, id) {
                    keyps.cancelEvent(ev);
                    if (frm.getItemType(id) == 'radio')
                        frm.setItemFocus(id, frm.getCheckedValue(id))
                    else
                        frm.setItemFocus(id)
                }


                keyps.radioNext = function (inp, ev, id, value) {
                    var frm = this
                    var hit = false;
                    var done = false;

                    frm.forEachItem(function (oId, oValue) {

                        if (done) {
                            return
                        }
                        if (id == oId) {

                            if (hit) {
                                frm.checkItem(id, oValue)
                                frm.setItemFocus(id, oValue)
                                keyps.cancelEvent(ev);
                                done = true;
                                return;
                            }

                            if (value == oValue)
                                hit = true;
                        }

                    })
                }


                keyps.radioPrior = function (inp, ev, id, value) {
                    var frm = this
                    var done = false;
                    var pValue = ""

                    frm.forEachItem(function (oId, oValue) {

                        if (done) {
                            return
                        }
                        if (id == oId) {
                            if (value != oValue)
                                pValue = oValue

                            if (value == oValue) {
                                frm.checkItem(id, pValue)
                                frm.setItemFocus(id, pValue)
                                keyps.cancelEvent(ev);
                                done = true;
                                return;
                            }
                        }

                    })
                }


                keyps.reverse = function (inp, ev, id, value) {
                    var frm = this
                    var frm = frm
                    var done = false;
                    var prev;

                    this.forEachItem(function (xid, rvalue) {

                        if (done) {
                            return
                        }

                        if (
                                (id == xid && prev != undefined) &&
                                (frm.getItemType(xid) == 'input' || frm.getItemType(xid) == 'checkbox' || frm.getItemType(xid) == 'radio' || frm.getItemType(xid) == 'select' || frm.getItemType(xid) == 'calendar' || frm.getItemType(xid) == 'button')
                                )
                        {
                            keyps.focus(frm, ev, prev)
                            done = true;
                            return
                        }

                        if (frm.getItemType(xid) == 'input' || frm.getItemType(xid) == 'checkbox' || frm.getItemType(xid) == 'radio' || frm.getItemType(xid) == 'select' || frm.getItemType(xid) == 'calendar' || frm.getItemType(xid) == 'button') {
                            prev = xid;
                            return
                        }

                    })

                    if (!done && prev != undefined) {
                        keyps.focus(frm, ev, prev)
                    }

                }


                keyps.forward = function (inp, ev, id, value) {
                    var frm = this
                    var hit = false;
                    var done = false;
                    var first, hitRadioName

                    this.forEachItem(function (xid, rvalue) {
                        if (done) {
                            return
                        }
                        if (hit) {

                            if (frm.getItemType(xid) == 'input' || frm.getItemType(xid) == 'checkbox' || frm.getItemType(xid) == 'select' || frm.getItemType(xid) == 'calendar' || frm.getItemType(xid) == 'button') {
                                keyps.focus(frm, ev, xid)
                                done = true;
                                return;
                            }

                            if (frm.getItemType(xid) == 'radio') {
                                if (hitRadioName == xid)
                                    return
                                keyps.focus(frm, ev, xid)
                                done = true;
                                return;
                            }
                            return
                        }

                        if (first == undefined) {
                            if (
                                    frm.getItemType(xid) == 'input' ||
                                    frm.getItemType(xid) == 'checkbox' ||
                                    frm.getItemType(xid) == 'select' ||
                                    frm.getItemType(xid) == 'calendar' ||
                                    frm.getItemType(xid) == 'radio'
                                    )
                                first = xid
                        }

                        if (hit == false) {
                            if (id == xid && (frm.getItemType(xid) == 'input' || frm.getItemType(xid) == 'checkbox' || frm.getItemType(xid) == 'select' || frm.getItemType(xid) == 'calendar' || frm.getItemType(xid) == 'button')) {
                                hit = true
                                return
                            }

                            if (frm.getItemType(xid) == 'radio' && id == xid && value == rvalue) {
                                hit = true
                                hitRadioName = xid
                                return
                            }
                        }
                    })

                    if (!done && first != undefined)
                        return keyps.focus(frm, ev, first)

                }


                keyps.handeler = function (inp, ev, id, value) {

                    if (ev.keyCode == 9 || ev.keyCode == 13) {

                        if (ev.keyCode == 13 && this.getItemType(id) == 'button') {
                            this.clickButton(id)
                            keyps.cancelEvent(ev);
                            return
                        }

                        if (ev.keyCode == 13 && inp.hasAttribute("rows"))
                            return

                        if (ev.shiftKey)
                            keyps.reverse.apply(this, arguments)
                        else
                            keyps.forward.apply(this, arguments)
                    }

                    if (this.getItemType(id) == 'radio') {
                        if (ev.keyCode == 39 || ev.keyCode == 40)
                            keyps.radioNext.apply(this, arguments)
                        else if (ev.keyCode == 37 || ev.keyCode == 38)
                            keyps.radioPrior.apply(this, arguments)
                    }

                }

                this.attachEvent("onKeyDown", keyps.handeler)
            }
        </script>
        <title>Order Manager v 1.0</title>
    </head>
    <body>        
        <div id="Layout_Container">            
        </div>
    </body>
    <script>
        var initializeObj = new com.ordermanager.home.OrderManagerHome();
    </script>
</html>
