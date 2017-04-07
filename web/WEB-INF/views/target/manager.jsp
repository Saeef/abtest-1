<%@ page import="java.util.Date" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2015/5/19
  Time: 11:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">

  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <meta name="description" content="Xenon Boostrap Admin Panel" />
  <meta name="author" content="" />

  <title>Vuclip - Target Manager</title>
  <link rel="stylesheet" href="${css}/main.css">
  <%@include file="../common/css.jsp"%>
  <link rel="stylesheet" href="${artDialog}/css/ui-dialog.css">
  <link rel="stylesheet" href="${assets}/js/select2/select2.css">
  <link rel="stylesheet" href="${assets}/js/select2/select2-bootstrap.css">
  <%--<link href="${resources}/multiselect/css/multi.css" rel="stylesheet" type="text/css" />--%>
  <link rel="stylesheet" type="text/css" href="${resources}/jquery-ui-multiselect/jquery.multiselect.css" />
  <%--<link rel="stylesheet" type="text/css" href="${resources}/jquery-ui-multiselect/jquery.multiselect.filter.css" />--%>
  <link rel="stylesheet" type="text/css" href="${resources}/jquery-ui-multiselect/demos/assets/style.css" />
  <link rel="stylesheet" type="text/css" href="${resources}/jquery-ui-multiselect/demos/assets/prettify.css" />
  <%@include file="../common/js.jsp"%>
  <script type="text/javascript" src="${assets}/js/datatables/js/jquery.dataTables.min.js"></script>
  <script type="text/javascript" src="${assets}/js/datatables/dataTables.bootstrap.js"></script>
  <script type="text/javascript" src="${assets}/js/datatables/yadcf/jquery.dataTables.yadcf.js"></script>
  <script type="text/javascript" src="${assets}/js/datatables/tabletools/dataTables.tableTools.min.js"></script>
  <script type="text/javascript" src="${resources}/datepicker/WdatePicker.js"></script>
  <script type="text/javascript" src="${resources}/jqueryui/jquery-ui.js"></script>
  <script type="text/javascript" src="${assets}/js/devexpress-web-14.1/js/globalize.min.js"></script>
  <script type="text/javascript" src="${assets}/js/devexpress-web-14.1/js/dx.chartjs.js"></script>
  <script type="text/javascript" src="${artDialog}/dist/dialog-min.js"></script>
  <script type="text/javascript" src="${assets}/js/select2/select2.min.js"></script>
  <script type="text/javascript" src="${assets}/js/selectboxit/jquery.selectBoxIt.min.js"></script>
  <script type="text/javascript" src="${resources}/jquery-ui-multiselect/demos/assets/prettify.js"></script>
  <script type="text/javascript" src="${resources}/jquery-ui-multiselect/src/jquery.multiselect.js"></script>
  <script type="text/javascript" src="${resources}/jquery-ui-multiselect/src/jquery.multiselect.filter.js"></script>
  
  
  <script type="text/javascript" src="${js}/target/target.js"></script>
  <style type="text/css">
    .ui-multiselect { margin:5px; height:30px }
  </style>
  <script type=text/javascript>

    function menuFix(){
      var sfEls = document.getElementById("nav").getElementsByTagName("li");
      for (var i=0; i<sfEls.length; i++){
        sfEls[i].onmouseover=function(){
          this.className+=(this.className.length>0? " ": "") + "sfhover";
        }
        sfEls[i].onMouseDown=function(){
          this.className+=(this.className.length>0? " ": "") + "sfhover";
        }
        sfEls[i].onMouseUp=function(){
          this.className+=(this.className.length>0? " ": "") + "sfhover";
        }
        sfEls[i].onmouseout=function(){
          this.className=this.className.replace(new RegExp("( ?|^)sfhover\\b"),
                  "");
        }
      }
    }
    window.onload=menuFix;
  </script>
</head>
<body>
<div style="margin-top: 0px;height: auto; width:1915px;background-color: #ffffff;height: 100px;"><!-- set fixed position by adding class "navbar-fixed-top" -->
  <div style="float: left;">
    <img src="${images}/vuclip_big.png" alt="vuclip logo" width="250px" height="100px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-size: 150%;">Vuclip Abtest Manager</span>
  </div>
  <div style="float: right;">
    <div style="float: left;font-size: 16px;margin-top: 40px;margin-right: 15px;"><span id="nowtimeid">&nbsp;</span></div>
    <div style="float: right;margin-top: 35px;margin-right: 25px;">
      <ul id="nav">
        <li>
          <a href="javascript:void(0);">
            <img src="${assets}/images/user-1.png" alt="user-image" class="img-circle img-inline userpic-32" width="28" />
						<span>
							${user.username}
							<i class="fa-angle-down"></i>
						</span>
          </a>
          <ul>
            <li>
              <a href="javascript:void(0);" id="userinfobtn">
                <i class="fa-user"></i>
                Profile
              </a>
            </li>
            <li class="last">
              <a href="/login/logout">
                <i class="fa-lock"></i>
                Logout
              </a>
            </li>
          </ul>
        </li>
      </ul>
    </div>
  </div>

</div>
<div class="page-container" style="position: relative;margin-top: 3px;height: 88%;width:1915px;"><!-- add class "sidebar-collapsed" to close sidebar by default, "chat-visible" to make chat appear always -->
  <div class="col-md-6" style="width: 100%">

    <ul class="nav nav-tabs">
      <li class="active">
        <a href="#projectmanager" data-toggle="tab">
          <span class="visible-xs"><i class="fa-home"></i></span>
          <span class="hidden-xs">ProjectManager</span>
        </a>
      </li>
      <li>
        <a href="#report" data-toggle="tab">
          <span class="visible-xs"><i class="fa-user"></i></span>
          <span class="hidden-xs">ChartsReport</span>
        </a>
      </li>
    </ul>

    <div class="tab-content" style="padding-bottom: 0px;">
      <div class="tab-pane active" id="projectmanager">

        <div class="tab-content" style="margin-bottom: 0px;">
          <div class="tab-pane active" id="abtestproject">

            <div class="panel panel-default">
                <div id="project_data">
                  <a id = "abtestaddpro" href="javascript:void(0)" class="btn btn-secondary btn-sm btn-icon icon-left" data-toggle="modal" data-target="#myModal">
                  	Add Project
                  </a>
                  <table width="1600px" class="table table-striped table-bordered" border="0" cellspacing="0" cellpadding="0" id="t_project_data">
                    <thead>
                    </thead>
                    <tfoot>
                    </tfoot>
                  </table>
                </div>
            </div>
          </div>
        </div>

      </div>
      <div class="tab-pane" id="report" style="margin-bottom: 0px;">

          <div>
            <ul class="nav nav-tabs">
              <li class="active">
                <a href="#abtestreport" data-toggle="tab">
                  <span class="visible-xs"><i class="fa-home"></i></span>
                  <span class="hidden-xs">Abtest</span>
                </a>
              </li>
              <li>
                <a href="#trackreport" data-toggle="tab">
                  <span class="visible-xs"><i class="fa-user"></i></span>
                  <span class="hidden-xs">Tracking</span>
                </a>
              </li>
              <li>
                <a href="#ratereport" data-toggle="tab">
                  <span class="visible-xs"><i class="fa-user"></i></span>
                  <span class="hidden-xs">Rate</span>
                </a>
              </li>
            </ul>
            <div class="tab-content" style="padding-bottom: 20px;">
              <div class="tab-pane active" id="abtestreport">

                <div style="height: 200px; width: 100%;">

                  <fieldset>
                      <legend>Conditions</legend>
                    <form role="form" class="form-horizontal">
                      <div class="form-group" style="margin-left: 0px;margin-right: 0px;">

                        <label for="abtesttimeunit" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Unit of Time:</label>
                        <div class="col-sm-2">
                          <select id="abtesttimeunit" style="width: 200px;height: 30px;" onchange="aUnitTime(0,this.value);changeCountryAndPageid(1);">
                            <option value ="d">Daily</option>
                            <option value ="h">Hourly</option>
                          </select>
                        </div>

                        <label for="abtestStartTime" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 5px 0px 0px 0px;">Time:</label>
                        <div class="col-sm-2">
                        	<input type="text" class="form-control" placeholder="Start Date" id="abtestStartTime" onfocus="WdatePicker({maxDate:startMaxDate})" />
                        </div>
                        <div class="col-sm-2">
                            <span id="adailyspan"><input type="text" class="form-control" placeholder="End Date" id="abtestEndTime" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'abtestStartTime\',{d:1})}',maxDate:maxDate})"/></span>
                        </div>

                      </div>
                    </form>
                    <form role="form" class="form-horizontal">
                      <div class="form-group" style="margin-left: 0px;margin-right: 0px;">
                        <label for="abtestprol" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Project:</label>
                        <div class="col-sm-2">
                          <select id="abtestprol" style="width: 200px;height: 30px;" onchange="changeTarget(1,this.value);">
                            <option value="a0">select...</option>
                          </select>
                        </div>

                        <label for="abtesttarl" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Target:</label>
                        <div class="col-sm-2">
                          <select id="abtesttarl" style="width: 200px;height: 30px;" onchange="changeFactor(this.value);showCountryAndPageId(this.value,$('#abtesttarl').find('option:selected').attr('country'),$('#abtesttarl').find('option:selected').attr('pageid'),'abtest');">
                            <option>select...</option>
                          </select>
                        </div>

                        <label for="abtestfacl" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Factor:</label>
                        <div class="col-sm-2">
                          <select class="form-control" id="abtestfacl" multiple>
                              
                          </select>
                        </div>
                      </div>
                    </form>
                    <form role="form" class="form-horizontal">
                    	<div class="form-group" style="margin-left: 0px;margin-right: 0px;">
                    	
                    	  <div id="acountryelem">
	                          <label for="acountry" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Country:</label>
	                          <div class="col-sm-2">
	                            <select id="acountry" style="width: 200px;height: 30px;">
	                              <option value="0">select...</option>
	                            </select>
	                          </div>
                          </div>
                          
                          <div id="apageidelem">
	                          <label for="apageid" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Pageid:</label>
	                          <div class="col-sm-2">
	                            <select id="apageid" style="width: 200px;height: 30px;">
	                              <option value="0">select...</option>
	                            </select>
	                          </div>
                          </div>
                          
                        </div>
                    </form>
                    <div style="margin-top:20px;">
	                    <button id="abteststa" class="btn btn-gray">Statistics</button>
	                    <button id="abtestrepexp" class="btn btn-gray">Export</button>
                    </div>
                  </fieldset>
                </div>
                <div class="panel-body">
                  <div id="abtestbar" style="height: 600px; width: 50%; float: left;"></div>
                  <div id="aoursofdaybar" style="height: 450px; width: 50%; float: right;"></div>
                  <div id="percentbar" style="height: 450px; width: 50%; float: right;"></div>
                </div>

              </div>
              <div class="tab-pane" id="trackreport">

                <div style="height: 200px; width: 100%;">
                  <fieldset>
                    <legend>Conditions</legend>
                    <form role="form" class="form-horizontal">
                      <div class="form-group" style="margin-left: 0px;margin-right: 0px;">

                        <label for="tracktimeunit" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Unit of Time:</label>
                        <div class="col-sm-2">
                          <select id="tracktimeunit" style="width: 200px;height: 30px;" onchange="aUnitTime(1,this.value);changeCountryAndPageid(0);">
                            <option value ="d">Daily</option>
                            <option value ="h">Hourly</option>
                          </select>
                        </div>

                        <label for="trackStartTime" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 5px 0px 0px 0px;">Time:</label>
                        <div class="col-sm-2">
                        	<input type="text" class="form-control" placeholder="Start Date" id="trackStartTime" onfocus="WdatePicker({maxDate:startMaxDate})" />
                        </div>
                        <div class="col-sm-2">
                            <span id="tdailyspan"><input type="text" class="form-control" placeholder="End Date" id="trackEndTime" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'trackStartTime\',{d:1})}',maxDate:maxDate})"/></span>
                        </div>

                      </div>
                    </form>
                    <form role="form" class="form-horizontal">
                      <div class="form-group" style="margin-left: 0px;margin-right: 0px;">
                        <label for="trackprol" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Project:</label>
                        <div class="col-sm-2">
                          <select id="trackprol" style="width: 200px;height: 30px;" onchange="changeTarget(0,this.value);">
                              <option value="t0">select...</option>
                          </select>
                        </div>

                        <label for="tracktarl" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Target:</label>
                        <div class="col-sm-2">
                          <select id="tracktarl" style="width: 200px;height: 30px;" onchange="showCountryAndPageId(this.value,this.country,this.pageid,'tracking');">
                            <option>select...</option>
                          </select>
                        </div>
                      </div>
                    </form>
                    <form role="form" class="form-horizontal">
                    	<div class="form-group" style="margin-left: 0px;margin-right: 0px;">
                    	
                    	  <div id="tcountryelem">
	                          <label for="tcountry" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Country:</label>
	                          <div class="col-sm-2">
	                            <select id="tcountry" style="width: 200px;height: 30px;">
	                              <option value="0">select...</option>
	                            </select>
	                          </div>
                          </div>
                          
                          <div id="tpageidelem">
	                          <label for="tpageid" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Pageid:</label>
	                          <div class="col-sm-2">
	                            <select id="tpageid" style="width: 200px;height: 30px;">
	                              <option value="0">select...</option>
	                            </select>
	                          </div>
                          </div>
                          
                        </div>
                    </form>
                    <div style="margin-top:20px;">
	                    <button id="tracksta" class="btn btn-gray">Statistics</button>
	                    <button id="trackrepexp" class="btn btn-gray">Export</button>
                    </div>
                  </fieldset>
                </div>
                <div class="panel-body">
                  <div id="trackbar" style="height: 450px; width: 50%;float: left;"></div>
                  <div id="toursofdaybar" style="height: 450px; width: 50%;float: right;"></div>
                </div>

              </div>

              <div class="tab-pane" id="ratereport">

                <div style="height: 200px; width: 100%;">
                  <fieldset>
                    <legend>Conditions</legend>
                    <form role="form" class="form-horizontal">
                      <div class="form-group" style="margin-left: 0px;margin-right: 0px;">

                        <label for="ratetimeunit" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Unit of Time:</label>
                        <div class="col-sm-2">
                          <select id="ratetimeunit" style="width: 200px;height: 30px;" onchange="aUnitTime(2,this.value);changeCountryAndPageid(2);">
                            <option value ="d">Daily</option>
                            <option value ="h">Hourly</option>
                          </select>
                        </div>

                        <label for="rateStartTime" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 5px 0px 0px 0px;">Time:</label>
                        <div class="col-sm-2">
                          <input type="text" class="form-control" placeholder="Start Date" id="rateStartTime" onfocus="WdatePicker({maxDate:startMaxDate})" />
                        </div>
                        <div class="col-sm-2">
                          <span id="ratedailyspan"><input type="text" class="form-control" placeholder="End Date" id="rateEndTime" onfocus="WdatePicker({minDate:'#F{$dp.$D(\'trackStartTime\',{d:1})}',maxDate:maxDate})"/></span>
                        </div>

                      </div>
                    </form>
                    <form role="form" class="form-horizontal">
                      <div class="form-group" style="margin-left: 0px;margin-right: 0px;">
                        <label for="rateprol" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Project:</label>
                        <div class="col-sm-2">
                          <select id="rateprol" style="width: 200px;height: 30px;" onchange="changeTarget(2,this.value);">
                            <option value="t0">select...</option>
                          </select>
                        </div>

                        <label for="ratetarl" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Target:</label>
                        <div class="col-sm-2">
                          <select id="ratetarl" style="width: 200px;height: 30px;" onchange="changeFactor(this.value);showCountryAndPageId(this.value,$('#ratetarl').find('option:selected').attr('country'),$('#ratetarl').find('option:selected').attr('pageid'),'rate');">
                            <option>select...</option>
                          </select>
                        </div>

                        <label for="ratefacl" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Factor:</label>
                        <div class="col-sm-2">
                          <select class="form-control" id="ratefacl" multiple>

                          </select>
                        </div>

                      </div>
                    </form>

                    <form role="form" class="form-horizontal">
                      <div class="form-group" style="margin-left: 0px;margin-right: 0px;">

                        <div id="ratecountryelem">
                          <label for="ratecountry" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Country:</label>
                          <div class="col-sm-2">
                            <select id="ratecountry" style="width: 200px;height: 30px;">
                              <option value="0">select...</option>
                            </select>
                          </div>
                        </div>

                        <div id="ratepageidelem">
                          <label for="ratepageid" class="col-sm-2 control-label" style="width: 80px;text-align: right;padding: 7px 0px 0px 0px;">Pageid:</label>
                          <div class="col-sm-2">
                            <select id="ratepageid" style="width: 200px;height: 30px;">
                              <option value="0">select...</option>
                            </select>
                          </div>
                        </div>

                      </div>
                    </form>

                  </fieldset>
                  <%--<fieldset>--%>
                    <%--<legend>Common Condition</legend>--%>
                      <%--<div style="margin-left: 0px;margin-right: 0px;padding: 5px;">--%>

                          <%--<div class="alert-warning" style="height:20px;font-size:16px;">The following conditions is common.</div>--%>
                          <%--<c:forEach var="commoncon" items="${commoncons}" varStatus="status">--%>
                            <%--<c:if test="${commoncon.type=='checkbox'&&commoncon.name=='UserGroup' }">--%>
                              <%--&lt;%&ndash;<div>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<label for="rate${commoncon.name}" id="labelrate${commoncon.name}" style="width: 100px;text-align: right;">${commoncon.name}:</label>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<input type="checkbox" id="rate${commoncon.name}">&ndash;%&gt;--%>
                              <%--<label for="rate${commoncon.name}" id="labelrate${commoncon.name}" style="width: 100px;text-align: right;">${commoncon.name}:</label>--%>
                              <%--<select id="rate${commoncon.name}" multiple="multiple" size="5" >--%>
                              <%--</select>--%>
                              <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
                            <%--</c:if>--%>
                            <%--<c:if test="${commoncon.type=='select' }">--%>
                              <%--&lt;%&ndash;<div>&ndash;%&gt;--%>
                                <%--<label for="rate${commoncon.name}" id="labelrate${commoncon.name}" style="width: 100px;text-align: right;">${commoncon.name}:</label>--%>
                                <%--<select id="rate${commoncon.name}">--%>

                                <%--</select>--%>
                              <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
                            <%--</c:if>--%>
                            <%--<c:if test="${commoncon.type=='multiselect' }">--%>
                              <%--&lt;%&ndash;<div id="div${commoncon.name}">&ndash;%&gt;--%>
                                <%--<label for="rate${commoncon.name}" id="labelrate${commoncon.name}" style="width: 100px;text-align: right;">${commoncon.name}:</label>--%>
                                <%--<select id="rate${commoncon.name}" multiple="multiple" size="5" >--%>
                                <%--</select>--%>
                              <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
                            <%--</c:if>--%>
                            <%--<c:if test="${status.count%3==0}"><br /></c:if>--%>
                          <%--</c:forEach>--%>

                      <%--</div>--%>
                  <%--</fieldset>--%>
                  <div style="margin-top:20px;">
                    <button id="ratesta" class="btn btn-gray">Statistics</button>
                    <button id="raterepexp" class="btn btn-gray">Export</button>
                  </div>
                </div>
                <div class="panel-body">
                  <div >
                    <div id="ratebar" style="height: 450px; width: 49%;float: left;"></div>
                    <div id="rateclickcountbar" style="height: 450px; width: 50%; float: right;"></div>

                  </div>
                  <div >
                    <div id="ratevpbar" style="height: 450px; width: 49%; float: left;"></div>
                    <div id="rateshowcountbar" style="height: 450px; width: 50%; float: right;"></div>
                  </div>
                  <div >
                    <div id="blank" style="height: 450px; width: 49%;float: left;"></div>
                    <div id="rateoursofdaybar" style="height: 450px; width: 50%;float: right;"></div>
                  </div>
                </div>
              </div>
            </div>
          </div>
      </div>
    </div>
  </div>
</div>
<div id="proForm" style="display: none;">
  <label for="proname" style="width: 80px;text-align: right;">Name:</label>
  <input type="text" id="proname" maxlength="20" style="width: 220px;"><br />
  <label for="status" style="width: 80px;text-align: right;">Active:</label>
  <select id="status"><option value="0" selected>Active</option><option value="1">InActive</option></select><br />
  <label for="prodesc" style="width: 80px;text-align: right;">Description:</label>
  <textarea id="prodesc" style="width: 220px;"></textarea>
</div>

<div id="addTargetForm" style="display: none; width:400px;">
  <label for="targetname" style="width: 80px;text-align: right;">Name:</label>
  <input type="text" id="targetname" maxlength="20" style="width: 313px;"><br />
  <fieldset>
    <legend>Target Condition</legend>
    <div class="alert-warning">The target properties,abtest or tracking,etc. and Which country will be used to report the charts.</div>
    <label for="country" style="width: 100px;text-align: right;">Country:</label>
    <input type="checkbox" id="country" checked="">
    <label for="pageid" style="width: 100px;text-align: right;">Page:</label>
    <input type="checkbox" id="pageid" checked=""><br />
    <label for="targettype" style="width: 80px;text-align: right;">Type:</label>
    <select id="targettype" onchange="changeCondition(this.value);"><option value="1" selected>abtest</option><option value="0" >tracking</option></select>
    <label for="tarstatus" style="width: 80px;text-align: right;">Status:</label>
    <select id="tarstatus"><option value="0" selected>Active</option><option value="1">InActive</option></select><br />
  </fieldset>
  <fieldset id="commoncondition">
    <legend>Rate Common Condition</legend>
    <div class="alert-warning">The conditions are used for rate,they are the common of factors.</div>
    <c:forEach var="commoncon" items="${commoncons}">
      <c:if test="${commoncon.type=='checkbox' }">
        <div>
          <label for="${commoncon.name}" id="label${commoncon.name}" style="width: 100px;text-align: right;">${commoncon.name}:</label>
          <input type="checkbox" id="${commoncon.name}" checked="">
        </div>
      </c:if>
      <c:if test="${commoncon.type=='select' }">
        <div>
          <label for="${commoncon.name}" id="label${commoncon.name}" style="width: 100px;text-align: right;">${commoncon.name}:</label>
          <select id="${commoncon.name}" style="margin:5px;">

          </select>
        </div>
      </c:if>
      <c:if test="${commoncon.type=='multiselect' }">
        <div id="div${commoncon.name}">
          <label for="${commoncon.name}" id="label${commoncon.name}" style="width: 100px;text-align: right;">${commoncon.name}:</label>
          <select id="${commoncon.name}" multiple="multiple" size="5" >
          </select>
        </div>
      </c:if>
    </c:forEach><br />
  </fieldset>
  <fieldset id="ratecondition">
    <legend>Only For Factors Conditions</legend>
    <div class="alert-warning">The following conditions are only used for grouping factors.</div>
    <c:forEach var="ratecon" items="${ratecons}">
      <label for="${ratecon.name}" style="width: 100px;text-align: right;">${ratecon.name}:</label>
      <input type="checkbox" id="${ratecon.name}" checked="">
    </c:forEach><br />
      <c:forEach var="ratehidcon" items="${ratehidcons}">
        <c:if test="${ratehidcon.type=='checkbox' }">
          <div>
            <label for="${ratehidcon.name}" id="label${ratehidcon.name}" style="width: 100px;text-align: right;display: none">${ratehidcon.name}:</label>
            <input type="checkbox" id="${ratehidcon.name}" checked="" style="display: none;">
          </div>
        </c:if>
        <c:if test="${ratehidcon.type=='select' }">
          <div>
            <label for="${ratehidcon.name}" id="label${ratehidcon.name}" style="width: 100px;text-align: right;display: none">${ratehidcon.name}:</label>
            <select id="${ratehidcon.name}" style="display: none;">

            </select>
          </div>
        </c:if>
        <c:if test="${ratehidcon.type=='multiselect' }">
          <div id="div${ratehidcon.name}" style="display: none;">
            <label for="${ratehidcon.name}" id="label${ratehidcon.name}" style="width: 100px;text-align: right;">${ratehidcon.name}:</label>
            <select id="${ratehidcon.name}" multiple="multiple" size="5" >
            </select>
          </div>
        </c:if>
      </c:forEach>
  </fieldset>

  <label for="targetdesc">Description:</label>
  <textarea id="targetdesc" style="width: 320px;margin-top:5px;"></textarea>
</div>

<div id="addFactorForm" style="display: none;">
  <input type="hidden" id="targetId_addFactor"/>
  <label for="targetname" style="width: 80px;text-align: right;">Name:</label>
  <input type="text" id="factorname" maxlength="20"><br />
  <div id="factorcon"></div>
  <label for="factordesc" style="width: 80px;text-align: right;">Description:</label>
  <textarea id="factordesc" style="width: 280px;"></textarea>
</div>
<div id="userInfo" style="display: none;">
  <div class="form-group">
    <input type="hidden" id="userid" name="userid" value="${user.userid}">
    <label class="control-label">Username</label>
    <input type="text" class="form-control" id="username" name="username" placeholder="Username" value="${user.username}" />
    <span id="name-error"></span>
  </div>
  <div class="form-group">
    <label class="control-label">Old Password</label>
    <input type="password" class="form-control" id="oldpassword" name="oldpassword" placeholder="Old Password" value = "" />
    <span id="oldpass-error"></span>
  </div>
  <div class="form-group">
    <label class="control-label">New Password</label>
    <input type="password" class="form-control" id="password" name="password" placeholder="New Password" value = "" />
    <span id="pass-error"></span>
  </div>
  <div class="form-group">
    <label class="control-label">Confirm New Password</label>
    <input type="password" class="form-control" id="cpassword" name="cpassword" placeholder="Confirm New Password" value = "" />
    <span id="cpass-error"></span>
  </div>
</div>

</body>
</html>