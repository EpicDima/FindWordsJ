package com.epicdima.findwords.solver

import com.epicdima.findwords.mask.Mask
import com.epicdima.findwords.mask.MaskType
import com.epicdima.findwords.trie.WordTrie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

private const val MAX_DEPTH = 3

class CoroutineSolver(
    linesSeparator: String,
    maskType: MaskType,
    wordTrie: WordTrie
) : DefaultSolver(linesSeparator, maskType, wordTrie) {

    override fun findFullMatches(matchedWords: List<WordAndMask>): Unit = runBlocking {
        val matrix = createWordAndMaskMatrix(matchedWords)

        withContext(Dispatchers.Default) {
            f22(originalMask.copy(), matrix, ArrayList(), 0)
        }
    }

    private suspend fun f22(
        mask: Mask,
        matrix: Array<Array<List<WordAndMask>>>,
        result: List<WordAndMask>,
        depth: Int,
    ) {
        coroutineScope {
            if (mask.isAllTrue()) {
                fullMatches.add(result)
                return@coroutineScope
            }

            var targetIndex = -1
            var minOptions = Int.MAX_VALUE

            for (i in 0..<rows * cols) {
                if (!mask[i]) {
                    var optionsCount = 0
                    for (wordAndMask in matrix[i / cols][i % cols]) {
                        if (mask.notIntersects(wordAndMask.mask)) {
                            optionsCount++
                        }
                    }

                    if (optionsCount < minOptions) {
                        minOptions = optionsCount
                        targetIndex = i
                        if (optionsCount <= 1) {
                            break
                        }
                    }
                }
            }

            if (targetIndex != -1) {
                val jobs = mutableListOf<Job>()
                for (positionWordAndMask in matrix[targetIndex / cols][targetIndex % cols]) {
                    if (mask.notIntersects(positionWordAndMask.mask)) {
                        val tempResult = mutableListOf<WordAndMask>()
                        tempResult.addAll(result)
                        tempResult.add(positionWordAndMask)
                        if (depth < MAX_DEPTH) {
                            jobs.add(launch {
                                f22(
                                    mask.copy().or(positionWordAndMask.mask),
                                    matrix,
                                    tempResult,
                                    depth + 1,
                                )
                            })
                        } else {
                            f2(
                                mask.copy().or(positionWordAndMask.mask),
                                matrix,
                                tempResult,
                            )
                        }
                    }
                }

                jobs.joinAll()
            }
        }
    }
}