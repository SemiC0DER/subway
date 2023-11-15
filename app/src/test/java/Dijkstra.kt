data class Edge(val destination: Int, val time: Int, val distance: Int, val cost: Int)

fun dijkstra(graph: Array<MutableList<Edge>>, start: Int, end: Int, criteria: String): Pair<Int, List<Int>> {
    val n = graph.size
    val dist = IntArray(n) { Int.MAX_VALUE }
    val prev = IntArray(n) { -1 }
    val comparator: Comparator<Int> = when (criteria) {
        "time" -> compareBy { dist[it] }
        "distance" -> compareBy { dist[it] }
        "cost" -> compareBy { dist[it] }
        else -> compareBy { dist[it] }
    }
    dist[start] = 0

    for (i in 0 until n) {
        var minDist = Int.MAX_VALUE
        var u = -1

        for (v in 0 until n) {
            if (dist[v] < minDist && prev[v] == -1) {
                u = v
                minDist = dist[v]
            }
        }

        if (u == -1) break

        prev[u] = dist[u]

        for (edge in graph[u]) {
            val v = edge.destination
            val weight = when (criteria) {
                "time" -> edge.time
                "distance" -> edge.distance
                "cost" -> edge.cost
                else -> edge.time
            }
            if (dist[u] + weight < dist[v]) {
                dist[v] = dist[u] + weight
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

    return Pair(dist[end], path)
}

fun main() {
    // 예시 역 이름과 ID 매핑
    val stationNameToID = mapOf(
        "101" to 0,
        "102" to 1,
        "103" to 2,
        "104" to 3,
        "105" to 4,
        "106" to 5,
        "107" to 6,
        "108" to 7,
        "109" to 8,
        "110" to 9,
        "111" to 10,
        "112" to 11,
        "113" to 12,
        "114" to 13,
        "115" to 14,
        "116" to 15,
        "117" to 16,
        "118" to 17,
        "119" to 18,
        "120" to 19,
        "121" to 20,
        "122" to 21,
        "123" to 22

    )

    // 예시 그래프 생성 (역의 개수, 간선 정보)
    val n = 906 // 역의 총 개수
    val graph = Array(n) { mutableListOf<Edge>() }

    // 간선 정보 추가 (출발역, 도착역, 시간, 거리, 비용)
    // 예시 데이터를 추가하거나 실제 데이터를 추가하세요.
    graph[0].add(Edge(1,200,500,200))
    graph[1].add(Edge(0,200,500,200))

    val startStationName = "출발역이름" // 출발역 이름
    val endStationName = "도착역이름" // 도착역 이름

    val startStation = stationNameToID[startStationName] ?: -1
    val endStation = stationNameToID[endStationName] ?: -1

    val criteria = "time" // "time", "distance", "cost" 중 하나 선택

    if (startStation != -1 && endStation != -1) {
        val result = dijkstra(graph, startStation, endStation, criteria)
        val shortestValue = result.first
        val path = result.second

        if (shortestValue != Int.MAX_VALUE) {
            val criteriaLabel = when (criteria) {
                "time" -> "시간"
                "distance" -> "거리"
                "cost" -> "비용"
                else -> "기준 없음"
            }
            println("$startStationName 에서 $endStationName 까지의 최단 $criteriaLabel: $shortestValue")
            println("경로: ${path.joinToString(" -> ")}")
        } else {
            println("경로가 존재하지 않습니다.")
        }
    } else {
        println("입력한 역 이름이 유효하지 않습니다.")
    }
}
