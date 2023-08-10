package com.epicdima.findwords.solver

import com.epicdima.findwords.mask.MaskType
import com.epicdima.findwords.trie.WordTrie

class DeepRecursionSolver(
    linesSeparator: String,
    maskType: MaskType,
    wordTrie: WordTrie
) : DefaultSolver(linesSeparator, maskType, wordTrie) {

    override fun findFullMatches(matchedWords: List<WordAndMask>) {
        val matrix = createWordAndMaskMatrix(matchedWords)
        val mask = originalMask.copy()
        val result = mutableListOf<WordAndMask>()

        val deepRecursiveFunction = DeepRecursiveFunction<Int, Unit> { startIndex ->
            if (mask.isAllTrue()) {
                fullMatches.add(result.toList())
                return@DeepRecursiveFunction
            }
            for (i in startIndex until rows * cols) {
                if (mask[i]) {
                    continue
                }
                for (positionWordAndMask in matrix[i / cols][i % cols]) {
                    if (mask.notIntersects(positionWordAndMask.mask)) {
                        result.add(positionWordAndMask)
                        mask.or(positionWordAndMask.mask)
                        callRecursive(i + 1)
                        result.removeLast()
                        mask.xor(positionWordAndMask.mask)
                    }
                }
                break
            }
        }
        deepRecursiveFunction.invoke(0)
    }
}