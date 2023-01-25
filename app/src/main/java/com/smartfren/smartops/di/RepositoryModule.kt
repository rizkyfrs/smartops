package com.smartfren.smartops.di

import com.smartfren.smartops.data.network.ApiService
import com.smartfren.smartops.ui.corefacility.ccfreport.repository.CCFReportRepository
import com.smartfren.smartops.ui.dailyactivity.repository.DailyActivityRepository
import com.smartfren.smartops.ui.history.repository.MyHistoryRepository
import com.smartfren.smartops.ui.home.repository.HomepageRepository
import com.smartfren.smartops.ui.login.repository.LoginRepository
import com.smartfren.smartops.ui.corefacility.nfmtiket.repository.NFMTicketRepository
import com.smartfren.smartops.ui.reportactivity.repository.ActivityReportRepository
import com.smartfren.smartops.ui.reportpm.repository.ReportPMRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @ExperimentalCoroutinesApi
    @Provides
    fun providesLoginRepository(apiService: ApiService): LoginRepository {
        return LoginRepository(apiService)
    }

    @ExperimentalCoroutinesApi
    @Provides
    fun providesHomepageRepository(apiService: ApiService): HomepageRepository {
        return HomepageRepository(apiService)
    }

    @Provides
    fun providesDailyActivityRepository(apiService: ApiService): DailyActivityRepository {
        return DailyActivityRepository(apiService)
    }

    @Provides
    fun providesMyHistoryReport(apiService: ApiService): MyHistoryRepository {
        return MyHistoryRepository(apiService)
    }

    @Provides
    fun providesActivityReport(apiService: ApiService): ActivityReportRepository {
        return ActivityReportRepository(apiService)
    }

    @Provides
    fun providesCFFActivityRepository(apiService: ApiService): CCFReportRepository {
        return CCFReportRepository(apiService)
    }

    @Provides
    fun providesNFMTicketRepository(apiService: ApiService): NFMTicketRepository {
        return NFMTicketRepository(apiService)
    }

    @Provides
    fun providesReportPMRepository(apiService: ApiService): ReportPMRepository {
        return ReportPMRepository(apiService)
    }
}