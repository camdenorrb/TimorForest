package me.camdenorrb.timorforest.node.impl

import me.camdenorrb.timorforest.node.base.NodeBase

data class TreeNode<T>(override val value: T, val left: TreeNode<T>?, val right: TreeNode<T>?) : NodeBase<T>