package com.hypeapps.instasplit.core.db

import com.hypeapps.instasplit.core.db.dao.ExpenseDao
import com.hypeapps.instasplit.core.db.dao.GroupDao
import com.hypeapps.instasplit.core.db.dao.GroupMemberDao
import com.hypeapps.instasplit.core.db.dao.UserDao
import com.hypeapps.instasplit.core.db.dao.UserExpenseDao

class InstaSplitRepository(
    private val userDao: UserDao,
    private val groupDao: GroupDao,
    private val expenseDao: ExpenseDao,
    private val GroupMemberDao: GroupMemberDao,
    private val UserExpenseDao: UserExpenseDao
) {
    // implement methods to interact with the database here (also with network if we implement that
}