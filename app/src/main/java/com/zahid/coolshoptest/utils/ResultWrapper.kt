package com.zahid.coolshoptest.utils

import com.zahid.coolshoptest.utils.Status.status

class ResultWrapper<T>(var status: status, var data: T?, var message: String)