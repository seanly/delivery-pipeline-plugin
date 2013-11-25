/*
This file is part of Delivery Pipeline Plugin.

Delivery Pipeline Plugin is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Delivery Pipeline Plugin is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Delivery Pipeline Plugin.
If not, see <http://www.gnu.org/licenses/>.
*/
package se.diabol.jenkins.pipeline;

import hudson.model.AutoCompletionCandidates;
import hudson.model.FreeStyleProject;
import hudson.util.FormValidation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.WithoutJenkins;
import org.kohsuke.stapler.StaplerRequest;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class PipelinePropertyTest {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    @Test
    @WithoutJenkins
    public void testDoCheckStageName() {
        PipelineProperty.DescriptorImpl d = new PipelineProperty.DescriptorImpl();
        assertEquals(FormValidation.Kind.OK, d.doCheckStageName("").kind);
        assertEquals(FormValidation.Kind.ERROR, d.doCheckStageName(" ").kind);
        assertEquals(FormValidation.Kind.OK, d.doCheckStageName("Stage").kind);
    }

    @Test
    @WithoutJenkins
    public void testDoCheckTaskName() {
        PipelineProperty.DescriptorImpl d = new PipelineProperty.DescriptorImpl();
        assertEquals(FormValidation.Kind.OK, d.doCheckTaskName("").kind);
        assertEquals(FormValidation.Kind.ERROR, d.doCheckTaskName(" ").kind);
        assertEquals(FormValidation.Kind.OK, d.doCheckTaskName("Task").kind);
    }

    @Test
    @WithoutJenkins
    public void testIsApplicable() {
        PipelineProperty.DescriptorImpl d = new PipelineProperty.DescriptorImpl();
        assertTrue(d.isApplicable(FreeStyleProject.class));

    }

    @Test
    @WithoutJenkins
    public void testNewInstanceEmpty() throws Exception {
        PipelineProperty.DescriptorImpl d = new PipelineProperty.DescriptorImpl();
        StaplerRequest request = Mockito.mock(StaplerRequest.class);
        when(request.getParameter("taskName")).thenReturn("");
        when(request.getParameter("stageName")).thenReturn("");
        assertNull(d.newInstance(request, null));
    }

    @Test
    @WithoutJenkins
    public void testNewInstanceBothSet() throws Exception {
        PipelineProperty.DescriptorImpl d = new PipelineProperty.DescriptorImpl();
        StaplerRequest request = Mockito.mock(StaplerRequest.class);
        when(request.getParameter("taskName")).thenReturn("Task");
        when(request.getParameter("stageName")).thenReturn("Stage");
        PipelineProperty p = d.newInstance(request, null);
        assertNotNull(p);
        assertEquals("Task", p.getTaskName());
        assertEquals("Stage", p.getStageName());
    }

    @Test
    public void testDoAutoCompleteStageName() throws Exception {
        PipelineProperty.DescriptorImpl d = new PipelineProperty.DescriptorImpl();
        FreeStyleProject build = jenkins.createFreeStyleProject("build");
        build.addProperty(new PipelineProperty("Build", "Build"));

        AutoCompletionCandidates c1 = d.doAutoCompleteStageName("B");
        assertEquals(c1.getValues().size(), 1);

        AutoCompletionCandidates c2 = d.doAutoCompleteStageName("A");
        assertEquals(c2.getValues().size(), 0);

        AutoCompletionCandidates c3 = d.doAutoCompleteStageName(null);
        assertEquals(c2.getValues().size(), 0);


    }

}