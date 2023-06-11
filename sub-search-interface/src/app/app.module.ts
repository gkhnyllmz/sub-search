import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {MenubarModule} from "primeng/menubar";
import {BrowserModule} from "@angular/platform-browser";
import { MovieComponent } from './movie/movie.component';
import {InputTextModule} from "primeng/inputtext";
import {FormsModule} from "@angular/forms";
import {ButtonModule} from "primeng/button";
import {DropdownModule} from "primeng/dropdown";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {RatingModule} from "primeng/rating";
import {InputTextareaModule} from "primeng/inputtextarea";
import {DataViewModule} from "primeng/dataview";
import {TagModule} from "primeng/tag";
import {ChipModule} from "primeng/chip";
import {HttpClient, HttpClientModule} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent,
    MovieComponent
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    MenubarModule,
    InputTextModule,
    FormsModule,
    ButtonModule,
    DropdownModule,
    BrowserAnimationsModule,
    RatingModule,
    InputTextareaModule,
    DataViewModule,
    TagModule,
    ChipModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
