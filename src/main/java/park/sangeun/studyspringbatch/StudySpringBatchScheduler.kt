package park.sangeun.studyspringbatch

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*

@EnableScheduling
@Component
@ConditionalOnProperty("study.enabledschedule")
class StudySpringBatchScheduler(
    private val jobLauncher: JobLauncher
) {
    @Autowired
    private lateinit var  concurrencyJob: Job

    @Scheduled(cron = "\${study.cron.concurrencyJob}")
    fun concurrencyJob() {
        val date = Date(System.currentTimeMillis())
        val format = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.KOREA)

        val jobParameters = JobParametersBuilder()
            .addString("START_TIME", format.format(date))
            .toJobParameters()
        executeJob(concurrencyJob, jobParameters)
    }

    private fun executeJob(job: Job, jobParameters: JobParameters) {
        jobLauncher.run(job, jobParameters)
    }
}