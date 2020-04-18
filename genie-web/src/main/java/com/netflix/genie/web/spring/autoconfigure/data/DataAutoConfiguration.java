/*
 *
 *  Copyright 2018 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.genie.web.spring.autoconfigure.data;

import com.netflix.genie.web.data.jpa.services.JpaPersistenceServiceImpl;
import com.netflix.genie.web.data.repositories.jpa.JpaAgentConnectionRepository;
import com.netflix.genie.web.data.repositories.jpa.JpaApplicationRepository;
import com.netflix.genie.web.data.repositories.jpa.JpaClusterRepository;
import com.netflix.genie.web.data.repositories.jpa.JpaCommandRepository;
import com.netflix.genie.web.data.repositories.jpa.JpaCriterionRepository;
import com.netflix.genie.web.data.repositories.jpa.JpaFileRepository;
import com.netflix.genie.web.data.repositories.jpa.JpaJobRepository;
import com.netflix.genie.web.data.repositories.jpa.JpaRepositories;
import com.netflix.genie.web.data.repositories.jpa.JpaTagRepository;
import com.netflix.genie.web.data.services.DataServices;
import com.netflix.genie.web.data.services.PersistenceService;
import com.netflix.genie.web.services.AttachmentService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Default auto configuration of data related services and beans for Genie.
 *
 * @author tgianos
 * @since 4.0.0
 */
@Configuration
// TODO: Create marker class for base classes scanning for compile time check
@EnableJpaRepositories("com.netflix.genie.web.data.repositories")
@EntityScan("com.netflix.genie.web.data.entities")
public class DataAutoConfiguration {

    /**
     * Provide a {@link DataServices} instance if one isn't already in the context.
     *
     * @param persistenceService The {@link PersistenceService} implementation to use
     * @return A {@link DataServices} instance
     */
    @Bean
    @ConditionalOnMissingBean(DataServices.class)
    public DataServices genieDataServices(final PersistenceService persistenceService) {
        return new DataServices(persistenceService);
    }

    /**
     * Provide a {@link JpaRepositories} container instance if one wasn't already provided.
     *
     * @param agentConnectionRepository The {@link JpaAgentConnectionRepository} instance
     * @param applicationRepository     The {@link JpaApplicationRepository} instance
     * @param clusterRepository         The {@link JpaClusterRepository} instance
     * @param commandRepository         The {@link JpaCommandRepository} instance
     * @param criterionRepository       The {@link JpaCriterionRepository} instance
     * @param fileRepository            The {@link JpaFileRepository} instance
     * @param jobRepository             The {@link JpaJobRepository} instance
     * @param tagRepository             The {@link JpaTagRepository} instance
     * @return A new {@link JpaRepositories} instance to simplify passing around all repositories
     */
    @Bean
    @ConditionalOnMissingBean(JpaRepositories.class)
    public JpaRepositories genieJpaRepositories(
        final JpaAgentConnectionRepository agentConnectionRepository,
        final JpaApplicationRepository applicationRepository,
        final JpaClusterRepository clusterRepository,
        final JpaCommandRepository commandRepository,
        final JpaCriterionRepository criterionRepository,
        final JpaFileRepository fileRepository,
        final JpaJobRepository jobRepository,
        final JpaTagRepository tagRepository
    ) {
        return new JpaRepositories(
            agentConnectionRepository,
            applicationRepository,
            clusterRepository,
            commandRepository,
            criterionRepository,
            fileRepository,
            jobRepository,
            tagRepository
        );
    }

    /**
     * Provide a default implementation of {@link PersistenceService} if no other has been defined.
     *
     * @param jpaRepositories   The {@link JpaRepositories} for Genie
     * @param attachmentService The {@link AttachmentService} implementation to use
     * @return A {@link JpaPersistenceServiceImpl} instance which implements {@link PersistenceService} backed by
     * JPA and a relational database
     */
    @Bean
    @ConditionalOnMissingBean(PersistenceService.class)
    public JpaPersistenceServiceImpl geniePersistenceService(
        final JpaRepositories jpaRepositories,
        final AttachmentService attachmentService
    ) {
        return new JpaPersistenceServiceImpl(jpaRepositories, attachmentService);
    }
}