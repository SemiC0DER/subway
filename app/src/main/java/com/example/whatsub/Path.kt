data class Edge(val destination: Int, val time: Int, val distance: Int, val cost: Int) //간선 데이터 클래스

data class DijkstraResult(val time: Int, val distance: Int, val cost: Int, val path: List<Int>) //결과값 데이터 클래스


val stationNames = """
        101
        102
        103
        104
        105
        106
        107
        108
        109
        110
        111
        112
        113
        114
        115
        116
        117
        118
        119
        120
        121
        122
        123
        201
        202
        203
        204
        205
        206
        207
        208
        209
        210
        211
        212
        213
        214
        215
        216
        301
        302
        303
        304
        305
        306
        307
        308
        401
        402
        403
        404
        405
        406
        407
        408
        409
        410
        411
        412
        413
        414
        415
        416
        417
        501
        502
        503
        504
        505
        506
        507
        601
        602
        603
        604
        605
        606
        607
        608
        609
        610
        611
        612
        613
        614
        615
        616
        617
        618
        619
        620
        621
        622
        701
        702
        703
        704
        705
        706
        707
        801
        802
        803
        804
        805
        806
        901
        902
        903
        904
    """ //역 이름 초기화

val edgesData = """
        101 102 200 500 200
        102 103 300 400 300
        103 104 1000 600 500
        104 105 500 200 340
        105 106 150 600 450
        106 107 320 200 120
        107 108 400 700 650
        108 109 800 350 200
        109 110 900 250 430
        110 111 500 650 120
        111 112 1000 400 890
        112 113 2000 500 800
        113 114 500 500 700
        114 115 220 400 540
        115 116 230 600 330
        116 117 300 200 280
        117 118 500 600 800
        118 119 480 200 1000
        119 120 500 700 2000
        120 121 400 350 700
        121 122 900 250 650
        122 123 300 650 440
        123 101 480 400 200
        101 201 1000 500 300
        201 202 250 500 500
        202 203 480 400 340
        203 204 400 600 450
        204 205 250 200 120
        205 206 500 600 650
        206 207 320 200 200
        207 208 250 700 430
        208 209 300 350 120
        209 210 150 250 890
        210 211 900 650 800
        211 212 320 400 700
        212 213 150 500 540
        213 214 500 500 330
        214 215 210 400 280
        215 216 150 600 800
        216 217 500 200 1000
        207 301 300 600 2000
        301 302 300 200 700
        302 303 480 700 650
        303 304 400 350 440
        304 123 250 250 200
        123 305 300 650 300
        305 306 250 400 500
        306 307 900 500 340
        307 308 480 500 450
        308 107 400 400 120
        104 401 1000 600 650
        401 307 150 200 200
        307 402 300 600 430
        402 403 210 200 120
        403 404 320 700 890
        404 405 210 350 800
        405 406 500 250 700
        406 407 300 650 540
        407 115 320 400 330
        115 408 480 500 280
        408 409 300 340 800
        409 410 480 500 1000
        410 411 300 400 2000
        411 412 900 600 700
        412 413 400 200 650
        413 414 430 600 440
        414 415 150 200 200
        415 416 1000 700 300
        416 417 500 350 500
        417 216 900 250 340
        209 501 320 650 450
        501 502 320 400 120
        502 503 430 500 650
        503 504 210 500 200
        504 122 320 400 430
        122 505 480 600 120
        505 506 300 200 890
        506 403 320 600 800
        403 507 300 200 700
        507 109 1000 700 540
        601 602 150 350 330
        602 121 700 250 280
        121 603 500 650 800
        603 604 300 400 1000
        604 605 430 200 2000
        605 606 480 300 700
        606 116 320 400 650
        116 607 250 200 440
        607 608 500 600 200
        608 609 700 200 300
        609 412 320 700 500
        412 610 1000 350 340
        610 611 700 250 450
        611 612 700 650 120
        612 613 150 400 650
        613 614 430 200 200
        614 615 500 300 430
        615 616 700 400 120
        616 417 480 200 890
        417 617 320 600 800
        617 618 300 200 700
        618 619 250 700 540
        619 620 700 350 330
        620 621 320 250 280
        621 622 480 650 800
        622 601 150 400 1000
        202 303 1000 200 2000
        303 503 700 300 700
        503 601 500 400 650
        601 701 430 200 440
        701 702 150 600 200
        702 703 600 200 300
        703 704 700 700 500
        704 705 250 350 340
        705 706 600 250 450
        706 416 300 650 120
        416 707 430 400 650
        707 614 480 200 200
        113 801 600 300 430
        801 802 1000 400 120
        802 803 700 200 890
        803 409 600 600 800
        409 608 500 200 700
        608 804 700 700 540
        804 805 150 350 330
        805 806 210 250 280
        806 705 600 650 800
        705 618 250 400 1000
        618 214 700 200 2000
        112 901 600 300 700
        901 406 300 400 650
        406 605 210 200 440
        605 902 480 600 280
        902 119 430 200 800
        119 903 1000 700 1000
        903 702 150 350 2000
        702 904 500 250 700
        904 621 250 650 650
        621 211 300 400 440
    """.trimIndent() //간선 정보 초기화

val stationMap = stationNames.split("\n")//역 이름을 인덱스로 매핑
    .mapNotNull { it.trim().takeIf { it.isNotEmpty() } }
    .distinct()
    .mapIndexed { index, name -> name to index }
    .toMap()

val graph = Array(stationMap.size) { mutableListOf<Edge>() }//역 갯수로 그래프 초기화

fun setGraph(){
    edgesData.split("\n").forEach { line ->
        val (start, end, time, distance, cost) = line.split(" ").map { it.toInt() }
        stationMap[start.toString()]?.let {
            stationMap[end.toString()]?.let { it1 ->
                addEdge(graph, it, it1, time, distance, cost)
            }
        }
    }
}//그래프에 모든 간선 정보를 담는 함수

fun addEdge(graph: Array<MutableList<Edge>>, start: Int, end: Int, time: Int, distance: Int, cost: Int) {
    graph[start].add(Edge(end, time, distance, cost))
    graph[end].add(Edge(start, time, distance, cost))//일방향 그래프로 바꿀시 삭제
    //println("Added edge: $start -> $end (Time: $time, Distance: $distance, Cost: $cost)") //정보가 올바르게 입력되었는지 테스트하는 코드
}//간선 정보 입력 함수

fun dijkstra(graph: Array<MutableList<Edge>>, start: Int, end: Int, criteria: String): DijkstraResult {//우선순위 큐를 사용한 다익스트라 길찾기 함수
    val n = graph.size
    val timeDist = IntArray(n) { Int.MAX_VALUE }
    val distanceDist = IntArray(n) { Int.MAX_VALUE }
    val costDist = IntArray(n) { Int.MAX_VALUE }
    val prev = IntArray(n) { -1 }
    val visited = BooleanArray(n)

    val comparator: Comparator<Int> = when (criteria) {//criteria에 따라 기준을 정함
        "time" -> compareBy { timeDist[it] }
        "distance" -> compareBy { distanceDist[it] }
        "cost" -> compareBy { costDist[it] }
        else -> compareBy { timeDist[it] }
    }
    timeDist[start] = 0
    distanceDist[start] = 0
    costDist[start] = 0

    for (i in 0 until n) {
        var minDist = Int.MAX_VALUE
        var u = -1

        for (v in 0 until n) {
            if (!visited[v] && (u == -1 || comparator.compare(v, u) < 0)) {
                u = v
                minDist = comparator.compare(v, u)
            }
        }

        // u가 -1이면 모든 정점을 방문한 것이므로 종료
        if (u == -1) break

        // 방문 표시
        visited[u] = true

        // u에 인접한 정점들에 대한 갱신은 그대로 유지
        for (edge in graph[u]) {
            val v = edge.destination
            val newTimeDist = timeDist[u] + edge.time
            val newDistanceDist = distanceDist[u] + edge.distance
            val newCostDist = costDist[u] + edge.cost

            if (!visited[v] && newTimeDist < timeDist[v]) {
                timeDist[v] = newTimeDist
                distanceDist[v] = newDistanceDist
                costDist[v] = newCostDist
                prev[v] = u
            }
        }
    }

    val path = mutableListOf<Int>()
    var current = end
    while (current != -1) {
        path.add(current)
        current = prev[current]
    }
    path.reverse()

    return DijkstraResult(timeDist[end], distanceDist[end], costDist[end], path)
}//우선순위 큐를 사용한 다익스트라 길찾기 함수

fun printStationNames(path: List<Int>): String { //텍스트 형식으로 역들의 목록과 환승지점을 반환하는 함수
    var printstation = ""
    printstation += "역 목록:"
    for (i in path.indices) {
        val stationIndex = path[i]
        val stationName = stationNames.split("\n")[stationIndex + 1].substring(8, 11)//stationNames에 공백이 있으므로 +1, 인덱스 8부터 10까지 문자열이 저장되므로 공백 제거

        if (i > 0 && i < path.size - 1) {//환승 조건 구현
            val prevStationName = stationNames.split("\n")[path[i - 1] + 1].substring(8, 11)
            val nextStationName = stationNames.split("\n")[path[i + 1] + 1].substring(8, 11)
            if (stationName[0] != prevStationName[0] && prevStationName[0] != nextStationName[0]) {
                if (!(stationName[0] != nextStationName[0]))
                    if(stationName != "417")//조건을 만족하더라도 이 역에서는 환승이 아님
                        printstation += "\n환승"
            }
        }
        printstation += "\n역: ${stationName}"
    }
    return printstation
}

fun printResult(result: DijkstraResult): String { //텍스트 형식으로 총시간, 총거리, 총비용을 반환하는 함수
    var printresult = ""
    val time = result.time
    val dist = result.distance
    val cost = result.cost


    if (time / 3600 > 0)
        printresult += "${time / 3600}시간 ${(time % 3600) / 60}분"
    else
        printresult += "${(time / 60)}분"

    if (dist / 1000 > 0)
        printresult += " ${dist / 1000}km ${(dist % 1000)}m"
    else
        printresult += " ${dist}m"

    printresult += " ${cost}원"
    return printresult
}
