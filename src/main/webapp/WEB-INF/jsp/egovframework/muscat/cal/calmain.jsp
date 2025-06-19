<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title><spring:message code="comCopSmtSim.calendar.title" /></title>

	<!-- ✅ Bootstrap 5.3.7 -->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css"
		  rel="stylesheet"
		  integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr"
		  crossorigin="anonymous">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
			integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
			crossorigin="anonymous"></script>

	<!-- ✅ FullCalendar -->
	<link href="https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.css" rel="stylesheet" />
	<script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.js"></script>
	<script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/locales/ko.js"></script>
	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

	<style>
		body {
			padding: 30px;
			background-color: #f8f9fa;
		}
		#calendar {
			max-width: 1500px;
			max-height: 800px;
			margin: 0 auto;
			background-color: #fff;
			padding: 20px;
			border-radius: 12px;
			box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
		}
	</style>
</head>
<body>
<div class="container">
	<h3 class="mb-4 text-center"><spring:message code="comCopSmtSim.calendar.title" /></h3>
	<div id="calendar"></div>
</div>
<script>
	let calendar;
	let selectedInfo;

	document.addEventListener('DOMContentLoaded', function () {
		let calendarEl = document.getElementById('calendar');

		calendar = new FullCalendar.Calendar(calendarEl, {
			initialView: 'dayGridMonth',
			locale: 'ko',
			themeSystem: 'bootstrap5',
			headerToolbar: {
				left: 'prev,next today',
				center: 'title',
				right: 'dayGridMonth,timeGridWeek,timeGridDay'
			},
			selectable: true,
			editable: true,
			select: function (info) {
				selectedInfo = info;
				$('#eventTitle').val('');
				$('#eventStart').val(info.startStr);
				$('#eventEnd').val(info.endStr);
				$('#eventAllDay').val(info.allDay);
				const modal = new bootstrap.Modal(document.getElementById('eventModal'));
				modal.show();
			},
			
		});

		calendar.render();

	});
</script>
</body>
</html>