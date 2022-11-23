package com.btcpiyush.ads.provider.models

import com.google.gson.annotations.SerializedName


data class RewardDialog(
    @SerializedName("is_reward_require") var isRewardRequire: Boolean? = null,
    @SerializedName("reward_message") var rewardMessage: String? = null
)