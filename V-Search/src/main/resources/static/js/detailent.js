$('.front').on('click', function() {
	$('.card').addClass('active');
})

$('.back').on('click', function() {
	$('.card').removeClass('active');
})
var curIndex = 0;
$(document).ready(function() {
	$('.front').on('click', function() {
		$('.front').hide();
		$('.back').show();
		$('.smyvideo').hide();
		$('.ognvideo').show();
		$('#smyscript').hide();
		$('.card').addClass('active');
		$('.smycontainer').find("input,select,button,textarea").prop("disabled", false);
		var player = videojs('VideoPlayer');
		player.pause();
	});

	$('.back').on('click', function() {
		$('.front').show();
		$('.back').hide();
		$('.smyvideo').show();
		$('.ognvideo').hide();
		$('.card').removeClass('active');
		$('#smyscript').show();
		$('.smycontainer').find("input,select,button,textarea").prop("disabled", true);
		var player = videojs('VideoPlayer2');
		player.pause();
	});
});
var player1, player2;
player1 = videojs("VideoPlayer", {
	controls: true,
	playsinline: true,
	preload: "auto",
	responsive: true,
	controlBar: {
		playToggle: true,
		pictureInPictureToggle: true,
		remainingTimeDisplay: true,
		progressControl: true
	}
});


player2 = videojs("VideoPlayer2", {
	controls: true,
	playsinline: true,
	preload: "auto",
	responsive: true,
	controlBar: {
		playToggle: true,
		pictureInPictureToggle: true,
		remainingTimeDisplay: true,
		progressControl: true
	}
});
player2.markers({
	markerTip: {
		display: true,
		text: function(marker) {
			return marker.text;
		},
		time: function(marker) {
			return marker.time;
		}
	},
	markers: []
});
function searchs() {
	$('#searchsmy').show()
	$('#summaryobject').hide()
}
function checks() {
	$('#searchsmy').hide()
	$('#summaryobject').show()
}
function calculateTimeInSeconds(timeComponents) {
	if (timeComponents.length === 3) {
		return parseInt(timeComponents[0]) * 3600 + parseInt(timeComponents[1]) * 60 + parseInt(timeComponents[2]);
	} else if (timeComponents.length === 2) {
		var hours = parseInt(timeComponents[0]);
		var minutes = parseInt(timeComponents[1]);
		return hours * 3600 + minutes * 60;
	}
	return parseInt(timeComponents[0]);
}


var searchInput = document.getElementById('searchObject');
var searchForm = $('#searchForm');
var searchDel = $('#searchDel');
var selectedCaptions = [];

$('#showSectionsButton').on('click', function() {
	var searchValue = searchInput.value;
	selectedCaptions = [];
	selectedDelCaptions = [];
	selectedCaptions.push(searchValue);
	$('#searchForm .searchField').each(function() {
		if ($(this).val() !== '') {
			selectedCaptions.push($(this).val());
		}
	});

	$('#searchDel .searchField').each(function() {
		if ($(this).val() !== '') {
			selectedDelCaptions.push($(this).val());
		}
	});
	showSelectedSections(selectedCaptions, selectedDelCaptions);
	player2.controlBar.progressControl.hide();
	player2.controlBar.remainingTimeDisplay.hide();
});

$('#addSearchFieldButton').on('click', function() {
	var newInput = $('<div class="inputWrapper"><input type="text" class="searchField" placeholder="단어 입력"><button type="button" class="removeFieldButton">-</button></div>');
	$('#searchForm').append(newInput);
});

$('#addSearchDelButton').on('click', function() {
	var newInput = $('<div class="inputWrapper"><input type="text" class="searchField" placeholder="단어 입력"><button type="button" class="removeFieldButton">-</button></div>');
	$('#searchDel').append(newInput);
});

$(document).on('click', '.removeFieldButton', function() {
	$(this).closest('.inputWrapper').remove();
});

function showSelectedSections(selectedCaptions, selectedDelCaptions) {
	var sections = [];
	var foundSection = false;
	var SMYresults = document.querySelectorAll('.script');

	SMYresults.forEach(function(result) {
		var caption = result.textContent.trim();

		var isMatched = selectedCaptions.some(function(selectedCaption) {
			return caption.includes(selectedCaption);
		});
		var isExcluded = selectedDelCaptions.some(function(selectedDelCaption) {
			return caption.includes(selectedDelCaption);
		});

		if (isMatched && !isExcluded) {
			var timeString = result.textContent.trim().split(' ')[0];
			var timeComponents = timeString.split(':');
			var time = calculateTimeInSeconds(timeComponents);
			sections.push({ start: time, end: time + 1 });
			foundSection = true;
		}
	});

	if (!foundSection) {
		alert("선택한 캡션이 포함된 섹션을 찾지 못했습니다.");
	}

	var mergedSections = mergeSections(sections);
	updateSections(mergedSections);
}

function updateSections(sections) {
	if (player2.markers && typeof player2.markers.destroy === 'function') {
		player2.markers.destroy();
	}
	createMarkers(sections);
}

function createMarkers(sections) {
	curIndex = 0;
	if (sections.length > 0) {
		player2.one('loadedmetadata', function() {
			player2.currentTime(sections[curIndex].start);
			player2.play();
		});
		player2.currentTime(sections[curIndex].start);
	}
	player2.off('timeupdate');
	player2.on('timeupdate', function() {
		var currentTime = player2.currentTime();
		if (curIndex < sections.length && currentTime >= sections[curIndex].end) {
			curIndex++;
			if (curIndex < sections.length) {
				player2.currentTime(sections[curIndex].start);
				player2.play();
			} else {
				player2.pause();
			}
		}
	});
}

document.querySelectorAll('.videoLink').forEach(function(element) {
	element.addEventListener('click', function() {
		var text = element.textContent.trim();
		var timeComponents = text.split(':');
		var time = 0;
		var time = calculateTimeInSeconds(timeComponents);
		timemove(parseInt(time));
	});
});
function timemove(time) {
	player2.currentTime(time);
}

$('#searchInput').on('input', function() {
	var searchTerm = $(this).val().toLowerCase();
	searchTerm = searchTerm.replace(/\s+/g, ''); // 공백 제거
	$(this).val(searchTerm); // 수정된 값을 입력 필드에 다시 설정

	if (searchTerm === '') {
		$('.videoLink').hide(); // 검색 요약 숨기기
	} else {
		// 검색어가 있을 때만 검색 실행
		$('#resultsContainer > div').each(function() {
			var text = $(this).find('.videoLink').attr('value').toLowerCase();
			if (text.includes(searchTerm)) {
				$(this).show();
				$(this).find('.videoLink').show();
			} else {
				$(this).hide();
				$(this).find('.videoLink').hide();
			}
		});
		$('#searchsmy').show();
	}
});


function mergeSections(sections) {
	sections.sort((a, b) => a.start - b.start);
	var mergeSections = [];
	var currSection = sections[0];
	for (var i = 1; i < sections.length; i++) {
		var nextSection = sections[i];
		var gap = nextSection.start - currSection.end;

		if (gap <= 3) {
			currSection.end = Math.max(currSection.end, nextSection.end);
		} else {
			mergeSections.push(currSection);
			currSection = nextSection;
		}
	}
	mergeSections.push(currSection);
	return mergeSections;
}

function subscribe(videoNo) {
	$.ajax({
		url: '/api/subscribe/' + videoNo,
		type: 'POST',
		success: function(response) {
			alert("구독되었습니다.");
			location.reload();
		},
		error: function(error) {
			console.error("구독 오류:", error);
		}
	});
}

function unsubscribe(videoNo) {
	$.ajax({
		url: '/api/subscribe/' + videoNo,
		type: 'DELETE',
		success: function(response) {
			alert("구독 취소되었습니다.");
			location.reload();
		},
		error: function(error) {
			console.error("구독 취소 오류:", error);
		}
	});
}

function recommend(videoNo) {
	$.ajax({
		url: '/recommend/toggle/' + videoNo,
		type: 'POST',
		success: function(response) {
			alert(response); // 서버에서 받은 메시지를 알림으로 표시
			location.reload(); // 페이지를 다시 로드하여 추천 수 업데이트
		},
		error: function(error) {
			console.error("추천 오류:", error);
		}
	});
}

