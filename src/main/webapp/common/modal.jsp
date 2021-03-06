<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
function showAlertDialog(title, msg) {
	$('#alertDialogTitle').html(title);
	$('#alertDialogBody').html(msg);
	$("#alertDialog").modal('show');
}

function showConfirmDialog(title, msg) {
	$('#confirmDialogTitle').html(title);
	$('#confirmDialogBody').html(msg);
	$("#confirmDialog").modal('show');
}

function showPopupDialog(title, url) {
	$("#popupDialog").modal('show');
	$("#popupDialogTitle").html(title);
	
	clearPopupDialogMessages();
	
	$("#popupDialogBody").html("");
	$("#popupDialogBody").load(url);
}

function displayPopupDialogErrorMessage(message, displayAlsoInFooter) {
	var errorMsgHtml = "<img src=\"${ctx}/images/iconWarning.gif\" alt=\"Warning\" class=\"icon\" />" 
					 + "&nbsp;" 
					 + message;
	$("#popupDialogErrorMessage").html(errorMsgHtml);
	
	if (displayAlsoInFooter) {
		$("#popupDialogErrorMessageFooter").html(errorMsgHtml);
	}
}

function displayPopupDialogSuccessMessage(message, displayAlsoInFooter) {
	var successMsgHtml = "<img src=\"${ctx}/images/iconInformation.gif\" alt=\"Information\" class=\"icon\" />" 
					   + "&nbsp;" 
					   + message;
	$("#popupDialogSuccessMessage").html(successMsgHtml);
	
	if (displayAlsoInFooter) {
		$("#popupDialogSuccessMessageFooter").html(successMsgHtml);
	}
}

function clearPopupDialogMessages() {
	clearPopupDialogErrorMessage();
	clearPopupDialogSuccessMessage();
}

function clearPopupDialogErrorMessage() {
	$("#popupDialogErrorMessage").html("");
	$("#popupDialogErrorMessageFooter").html("");
}

function clearPopupDialogSuccessMessage() {
	$("#popupDialogSuccessMessage").html("");
	$("#popupDialogSuccessMessageFooter").html("");
}
</script>

<div class="modal fade" id="alertDialog" role="dialog" data-backdrop="static" data-keyboard="false">
	<div class="modal-dialog" style="width:30% !important">
		<div class="modal-content">
		 	<div class="modal-header" id="alertDialogHeader">
		 		<h4 class="modal-title" id="alertDialogTitle">Alert</h4>
		 	</div>	
			<div class="modal-body" id="alertDialogBody"></div>
			<div class="modal-footer" id="alertDialogFooter">
			   <button type="button" data-dismiss="modal" class="btn btn-primary" id="alertDialogOk">OK</button>
			</div>
		</div>
	</div>
</div>

<div class="modal fade" id="confirmDialog" role="dialog" data-backdrop="static" data-keyboard="false">
	<div class="modal-dialog" style="width:50% !important">
		<div class="modal-content">
		 	<div class="modal-header" id="confirmDialogHeader">
		 		<h4 class="modal-title" id="confirmDialogTitle">Confirm</h4>
		 	</div>	
			<div class="modal-body" id="confirmDialogBody"></div>
			<div class="modal-footer" id="confirmDialogFooter">
			   <button type="button" data-dismiss="modal" class="btn btn-primary" id="confirmDialogYes">Yes</button>
			   <button type="button" data-dismiss="modal" class="btn btn-primary" id="confirmDialogNo">No</button>
			   <button type="button" data-dismiss="modal" class="btn" id="confirmDialogCancel">Cancel</button>
       		</div>
		</div>
	</div>
</div>

<div class="modal fade" id="popupDialog" role="dialog" data-backdrop="static" data-keyboard="false">
	<div class="modal-dialog" style="width:60% !important">
		<div class="modal-content">
		 	<div class="modal-header" id="popupDialogHeader">
        		<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
       			<h4 class="modal-title" id="popupDialogTitle">Title</h4>
       			<div id="popupDialogMessage">
       				<div id="popupDialogErrorMessage" style="color:red; font-size:14px; vertical-align:center;"></div>
       				<div id="popupDialogSuccessMessage" style="color:green; font-size:14px; vertical-align:center;"></div>
       			</div>
      		 </div>	
			<div class="modal-body" id="popupDialogBody"></div>
			<div class="modal-footer" id="popupDialogFooter">
				<div id="popupDialogErrorMessageFooter" style="color:red; font-size:14px; vertical-align:center; text-align:left;"></div>
       			<div id="popupDialogSuccessMessageFooter" style="color:green; font-size:14px; vertical-align:center;text-align:left;"></div>
       		</div>
		</div>
	</div>
</div>

<script type="text/javascript">
$("#alertDialog").on("hidden.bs.modal", function(e) {
	$("#alertDialogTitle").html("");
	$("#alertDialogBody").html("");
});

$("#confirmDialog").on("hidden.bs.modal", function(e) {
	$("#confirmDialogTitle").html("");
	$("#confirmDialogBody").html("");
});	

$("#popupDialog").on("hidden.bs.modal", function(e) {
	$("#popupDialogTitle").html("");
	$("#popupDialogBody").html("");
	
	clearPopupDialogMessages();
});	
</script>