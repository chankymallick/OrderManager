
            //console.log(JSON.stringify(Language));
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