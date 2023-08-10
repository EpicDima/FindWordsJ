package com.epicdima.findwords.solver

import com.epicdima.findwords.mask.Mask
import com.epicdima.findwords.mask.MaskType
import com.epicdima.findwords.trie.WordTrie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class CoroutineSolver(
    linesSeparator: String,
    maskType: MaskType,
    wordTrie: WordTrie
) : DefaultSolver(linesSeparator, maskType, wordTrie) {

    override fun findFullMatches(matchedWords: List<WordAndMask>): Unit = runBlocking {
        val matrix = createWordAndMaskMatrix(matchedWords)

        withContext(Dispatchers.Default) {
            f22(originalMask.copy(), matrix, 0, ArrayList())
        }
    }

    private suspend fun f22(
        mask: Mask,
        matrix: Array<Array<List<WordAndMask>>>,
        startIndex: Int,
        result: List<WordAndMask>
    ) {
        coroutineScope {
            if (mask.isAllTrue()) {
                fullMatches.add(result)
                return@coroutineScope
            }
            for (i in startIndex until rows * cols) {
                if (mask[i]) {
                    continue
                }
                val jobs = mutableListOf<Job>()
                for (positionWordAndMask in matrix[i / cols][i % cols]) {
                    if (mask.notIntersects(positionWordAndMask.mask)) {
                        jobs.add(launch(Dispatchers.Default) {
                            val tempResult = mutableListOf<WordAndMask>()
                            tempResult.addAll(result)
                            tempResult.add(positionWordAndMask)
                            f2(mask.copy().or(positionWordAndMask.mask), matrix, i + 1, tempResult)
                        })
                    }
                }

                jobs.forEach { it.join() }
                break
            }
        }
    }
}