package com.github.pockethub.android.ui.item.issue

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import com.github.pockethub.android.R
import com.github.pockethub.android.core.issue.IssueUtils
import com.github.pockethub.android.ui.StyledText
import com.github.pockethub.android.util.AvatarLoader
import com.meisolsson.githubsdk.model.Issue
import com.meisolsson.githubsdk.model.IssueState
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.issue_details.*
import kotlinx.android.synthetic.main.issue_number.*
import kotlinx.android.synthetic.main.repo_issue_item.*

open class IssueItem @JvmOverloads constructor(private val avatarLoader: AvatarLoader, val issue: Issue, private val showLabels: Boolean = true) : Item(issue.id()!!) {

    override fun getLayout() = R.layout.repo_issue_item

    override fun bind(holder: ViewHolder, position: Int) {
        val labels = issue.labels()
        val labelViews = listOf(holder.v_label0, holder.v_label1, holder.v_label2, holder.v_label3, holder.v_label4, holder.v_label5, holder.v_label6, holder.v_label7)
        if (showLabels && labels != null && !labels.isEmpty()) {
            labelViews.forEachIndexed { i, labelView ->
                if (i >= 0 && i < labels.size) {
                    val label = labels[i]
                    if (!TextUtils.isEmpty(label.color())) {
                        labelView.setBackgroundColor(Color.parseColor('#' + label.color()!!))
                        labelView.visibility = View.VISIBLE
                        return@forEachIndexed
                    }
                }

                labelView.visibility = View.GONE
            }
        } else {
            labelViews.forEach { it.visibility = View.GONE }
        }

        val numberText = StyledText()
        numberText.append(issue.number().toString())
        if (IssueState.Closed == issue.state()) {
            numberText.strikethroughAll()
        }

        holder.tv_issue_number.text = numberText

        avatarLoader.bind(holder.iv_avatar, issue.user())

        if (IssueUtils.isPullRequest(issue)) {
            holder.tv_pull_request_icon.visibility = View.VISIBLE
        } else {
            holder.tv_pull_request_icon.visibility = View.GONE
        }

        holder.tv_issue_title.text = issue.title()
        holder.tv_issue_comments.text = issue.comments().toString()

        val reporterText = StyledText()
        reporterText.bold(issue.user()!!.login())
        reporterText.append(' ')
        reporterText.append(issue.createdAt())
        holder.tv_issue_creation.text = reporterText
    }
}
